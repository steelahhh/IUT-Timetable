package com.alefimenko.iuttimetable.presentation.schedule.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.schedule.groups.GroupsFeature.Event
import com.alefimenko.iuttimetable.presentation.schedule.groups.GroupsFeature.GroupsModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding3.view.clicks
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.rx2.RxConnectables
import com.spotify.mobius.rx2.RxMobius
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.screen_groups.*
import org.koin.android.ext.android.get

/**
 * Using fragments alongside the Conductor Controllers
 *  is probably a really weird and questionable decision,
 *  but it's the easiest way to implement the complete functionality right now.
 *
 * TODO: Refactor to either Conductor dialog,
 *  or integrate it into the BottomSheet into the [com.alefimenko.iuttimetable.presentation.schedule.ScheduleView]
 */
class GroupsFragment(
    val onGroupListener: (groupId: Int) -> Unit = {},
    val onGroupDeleteListener: (groupId: Int) -> Unit = {}
) : BottomSheetDialogFragment(), GroupsView, OnGroupClickListener {

    private val controller: MobiusLoop.Controller<GroupsModel, Event> =
        MobiusAndroid.controller(
            RxMobius.loop(
                GroupsFeature.GroupsUpdater,
                GroupsFeature.GroupsEffectHandler(get(), get(), get(), this).create()
            ).init(GroupsFeature.GroupsInitializer).logger(AndroidLogger.tag("Groups")),
            GroupsModel()
        )

    private val groupsAdapter = GroupAdapter<GroupieViewHolder>()

    private val connector = RxConnectables.fromTransformer(::connect)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller.connect(connector)
        setupViews()
    }

    override fun onResume() {
        super.onResume()
        controller.start()
    }

    override fun onPause() {
        controller.stop()
        super.onPause()
    }

    override fun onDestroyView() {
        controller.disconnect()
        super.onDestroyView()
    }

    override fun dismissDialog() = dismiss()

    private fun connect(models: Observable<GroupsModel>): Observable<Event> {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable += models.distinctUntilChanged().subscribe(::render)

        return Observable.mergeArray<Event>(
            addGroupButton.clicks().map {
                Event.NavigateToOpenGroup
            }
        ).doOnDispose {
            compositeDisposable.dispose()
        }
    }

    private fun render(model: GroupsModel) {
        groupsProgress.isGone = !model.isLoading || model.isError

        groupsAdapter.clear()
        groupsAdapter.addAll(model.groups.map { (group, institutes) ->
            GroupUi(
                group.id,
                group.name,
                institutes.first().name,
                group.semester,
                group.id == model.currentGroup,
                this
            )
        })
    }

    private fun setupViews() {
        groupsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupsAdapter
        }
    }

    override fun onClick(item: GroupUi) {
        dismiss()
        onGroupListener(item.id)
    }

    override fun delete(item: GroupUi) {
        dismiss()
        onGroupDeleteListener(item.id)
    }
}

interface OnGroupClickListener {
    fun onClick(item: GroupUi)
    fun delete(item: GroupUi)
}
