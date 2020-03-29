package com.alefimenko.iuttimetable.presentation.ribs.schedule

import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.LayoutRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.schedule.model.ClassItem
import com.alefimenko.iuttimetable.presentation.schedule.model.EmptyDayItem
import com.alefimenko.iuttimetable.presentation.schedule.model.HeaderItem
import com.alefimenko.iuttimetable.presentation.schedule.model.Position
import com.alefimenko.iuttimetable.presentation.schedule.model.ScheduleInfoHeaderItem
import com.alefimenko.iuttimetable.presentation.schedule.model.toClassItem
import com.badoo.ribs.customisation.inflate
import com.jakewharton.rxrelay2.PublishRelay
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.rib_schedule.view.*
import kotlinx.android.synthetic.main.screen_schedule.*

/*
 * Author: steelahhh
 * 29/3/20
 */

class ScheduleViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<ScheduleView.Event> = PublishRelay.create()
) : ScheduleView,
    ObservableSource<ScheduleView.Event> by events,
    Consumer<ScheduleView.ViewModel> {

    private val itemAdapter = GroupAdapter<GroupieViewHolder>()
    private val headerIndices = mutableListOf<Int>()

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_schedule
    ) : ScheduleView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> ScheduleView = {
            ScheduleViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    init {
        with(androidView) {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = itemAdapter
            }
            toolbar.apply {
                replaceMenu(R.menu.schedule_menu)
                changeMenuColors()
            }

            scheduleChangeWeekButton.setOnClickListener {
                events.accept(ScheduleView.Event.ChangeWeek)
            }
        }
    }

    override fun accept(vm: ScheduleView.ViewModel) = with(androidView) {
        scheduleStubView.apply {
            isVisible = vm.isError
            textRes = R.string.schedule_loading_error
            onRetryClick = { /* insideEvents.onNext(ScheduleFeature.Event.DownloadSchedule) */ }
        }

        itemAdapter.clear()
        val items = Section(
            ScheduleInfoHeaderItem(
                vm.schedule?.groupTitle ?: "",
                vm.schedule?.semester ?: "",
                vm.currentWeek == 0
            )
        )
        vm.schedule?.weekSchedule?.get(vm.selectedWeek)?.let { days ->
            headerIndices.clear()
            days.forEachIndexed { dayIndex, list ->
                headerIndices.add(items.itemCount.takeIf { it != 1 } ?: 0)
                items.add(Section().apply {
                    setHeader(
                        HeaderItem(
                            dayIndex,
                            true,
                            list.firstOrNull()?.date ?: Constants.EMPTY_ENTRY
                        )
                    )
                    if (list.isEmpty()) add(EmptyDayItem())
                    else addAll(list.mapIndexed { listIdx, classEntry ->
                        val position = when {
                            list.size == 1 -> Position.SINGLE
                            listIdx == 0 -> Position.FIRST
                            listIdx == list.size - 1 -> Position.LAST
                            else -> Position.OTHER
                        }
                        classEntry.toClassItem(
                            position,
                            onClassMenuClick = { classUi, view ->
                                openPopupMenu(classUi, view, listIdx, dayIndex, vm.selectedWeek)
                            })
                    })
                })
            }
        }
        scheduleChangeWeekButton.isGone = vm.isError || vm.isLoading
        scheduleChangeWeekButton.text =
            vm.schedule?.weeks?.get(vm.selectedWeek) ?: androidView.context.getString(R.string.menu_change_week)
        itemAdapter.add(items)
        itemAdapter.notifyDataSetChanged()
        progressBar.isGone = !vm.isLoading
        if (itemAdapter.itemCount != 0 && headerIndices.isNotEmpty()) {
            val smoothScroller = object : LinearSmoothScroller(androidView.context) {
                override fun getVerticalSnapPreference() = SNAP_TO_START
            }
            smoothScroller.targetPosition = headerIndices[2]
            recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun openPopupMenu(
        classItem: ClassItem,
        view: View,
        classIndex: Int,
        dayIndex: Int,
        weekIndex: Int
    ) {
        PopupMenu(view.context, view).apply {
            menuInflater.inflate(R.menu.class_entry_menu, menu)
            setOnMenuItemClickListener {
                events.accept(ScheduleView.Event.ChangeClassVisibility(classIndex, dayIndex, weekIndex))
                true
            }
            show()
            menu.findItem(R.id.hide_class).isVisible = !classItem.hidden
            menu.findItem(R.id.restore_class).isVisible = classItem.hidden
        }
    }
}
