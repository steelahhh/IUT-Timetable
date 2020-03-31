package com.alefimenko.iuttimetable.presentation.ribs.groups

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.alefimenko.iuttimetable.extension.getColorFromAttr
import com.alefimenko.iuttimetable.extension.getDimen
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.data.GroupUi
import com.alefimenko.iuttimetable.presentation.data.OnGroupClickListener
import com.alefimenko.iuttimetable.presentation.ribs.groups.GroupsView.Event
import com.alefimenko.iuttimetable.presentation.ribs.groups.GroupsView.ViewModel
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.jakewharton.rxrelay2.PublishRelay
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.rib_groups.view.*

interface GroupsView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        data class OnGroupClicked(val group: GroupUi) : Event()
        data class OnDeleteGroup(val group: GroupUi) : Event()
        object Dismiss : Event()
        object OnAddGroupClick : Event()
    }

    data class ViewModel(
        val groups: List<GroupUi>
    )

    interface Factory : ViewFactory<Nothing?, GroupsView>
}

class GroupsViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : GroupsView,
    ObservableSource<Event> by events,
    Consumer<ViewModel>, OnGroupClickListener {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_groups
    ) : GroupsView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> GroupsView = {
            GroupsViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private val groupsAdapter = GroupAdapter<GroupieViewHolder>()

    init {
        val context = androidView.context
        val radius = context.getDimen(R.dimen.corner_radius_xh)
        val shapePathModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .build()
        val drawable = MaterialShapeDrawable(shapePathModel).apply {
            setTint(context.getColorFromAttr(R.attr.background_color))
        }
        with(androidView) {
            addGroupButton.setOnClickListener { events.accept(Event.OnAddGroupClick) }
            setOnClickListener { events.accept(Event.Dismiss) }
            groupsContainer.background = drawable
            groupsRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = groupsAdapter
            }
        }
    }

    override fun accept(vm: ViewModel) {
        val groups = vm.groups.map { it.copy(clickListener = this) }
        groupsAdapter.updateAsync(groups)

        androidView.groupsRecycler.updateLayoutParams<ConstraintLayout.LayoutParams> {
            height = if (groups.size <= 2) WRAP_CONTENT
            else androidView.context.getDimen(R.dimen.item_height_xxl).toInt()
        }
    }

    override fun onClick(group: GroupUi) = events.accept(Event.OnGroupClicked(group))

    override fun delete(group: GroupUi) = events.accept(Event.OnDeleteGroup(group))
}
