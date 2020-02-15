package com.alefimenko.iuttimetable.presentation.pickgroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.alefimenko.iuttimetable.base.KotlinView
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.Event
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.Model
import com.alefimenko.iuttimetable.presentation.pickgroup.model.Group
import com.alefimenko.iuttimetable.presentation.pickgroup.model.GroupItem
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.spotify.mobius.Connectable
import com.spotify.mobius.rx2.RxConnectables
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.screen_pick_group.*

/*
 * Created by Alexander Efimenko on 13/2/20.
 */

class PickGroupView(
    inflater: LayoutInflater,
    container: ViewGroup
) : KotlinView(R.layout.screen_pick_group, inflater, container) {

    private val groupsAdapter = GroupAdapter<GroupieViewHolder>()

    private val insideEvents = PublishSubject.create<Event>()
    val connector: Connectable<Model, Event> = RxConnectables.fromTransformer(::connect)

    fun connect(models: Observable<Model>): Observable<Event> {
        setupViews()

        val cd = CompositeDisposable()
        cd += models.distinctUntilChanged().subscribe(::render)

        return Observable.mergeArray<Event>(
            insideEvents,
            errorView.retryClicks().map { Event.LoadGroups },
            searchView.queryTextChanges()
                .distinctUntilChanged()
                .map {
                    Event.QueryChanged(it.toString())
                }
        ).doOnDispose {
            tearDown()
            cd.dispose()
        }
    }

    private fun render(value: Model) = with(value) {
        renderContent()
        renderError()
        toolbar.changeMenuColors()
    }

    private fun Model.renderContent() {
        progressBar.isVisible = isLoading
        recycler.isVisible = !isLoading

        if (groups.isNotEmpty()) {
            groupsAdapter.setOnItemClickListener { group, _ ->
                require(group is GroupItem)
                insideEvents.onNext(Event.GroupSelected(Group(group.id, group.label)))
            }

            groupsAdapter.clear()
            groupsAdapter.updateAsync(filteredGroups)
        }
    }

    private fun Model.renderError() {
        if (isError) groupsAdapter.clear()

        errorView.apply {
            isVisible = isError
            retryVisible = true
            textRes = R.string.group_loading_error
        }

        if (groups.isNotEmpty()) {
            if (filteredGroups.isNotEmpty()) {
                errorView.isGone = true
            } else {
                errorView.apply {
                    isVisible = true
                    textRes = R.string.search_error
                    retryVisible = false
                }
            }
        }
    }

    private fun setupViews() {
        recycler.run {
            layoutManager = LinearLayoutManager(context)
            adapter = groupsAdapter
        }
    }
}
