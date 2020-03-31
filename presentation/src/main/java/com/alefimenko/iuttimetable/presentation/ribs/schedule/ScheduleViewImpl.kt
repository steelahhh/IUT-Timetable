package com.alefimenko.iuttimetable.presentation.ribs.schedule

import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.LayoutRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.data.model.ClassItem
import com.alefimenko.iuttimetable.presentation.data.model.EmptyDayItem
import com.alefimenko.iuttimetable.presentation.data.model.HeaderItem
import com.alefimenko.iuttimetable.presentation.data.model.Position
import com.alefimenko.iuttimetable.presentation.data.model.ScheduleInfoHeaderItem
import com.alefimenko.iuttimetable.presentation.data.model.toClassItem
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleView.Event
import com.badoo.ribs.customisation.inflate
import com.jakewharton.rxrelay2.PublishRelay
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.rib_schedule.view.*

/*
 * Author: steelahhh
 * 29/3/20
 */

class ScheduleViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ScheduleView,
    ObservableSource<Event> by events,
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

    override fun openWeekPickerDialog(weeks: List<String>, selectedWeek: Int) {
        MaterialDialog(androidView.context).show {
            title(res = R.string.pick_week_dialog_title)
            listItemsSingleChoice(
                items = weeks,
                initialSelection = selectedWeek,
                selection = { _, index, _ ->
                    events.accept(Event.SwitchToWeek(index))
                }
            )
        }
    }

    init {
        with(androidView) {
            recyclerView.run {
                layoutManager = LinearLayoutManager(context)
                adapter = itemAdapter
            }
            toolbar.run {
                replaceMenu(R.menu.schedule_menu)
                setNavigationOnClickListener { events.accept(Event.OnMenuClick) }
                setOnMenuItemClickListener { menuItem ->
                    if (menuItem.itemId == R.id.action_settings) events.accept(Event.OnSettingsClick)
                    true
                }
                changeMenuColors()
            }

            scheduleChangeWeekButton.setOnClickListener {
                events.accept(Event.ChangeWeek)
            }
        }
    }

    override fun accept(vm: ScheduleView.ViewModel) = with(androidView) {
        scheduleStubView.apply {
            isVisible = vm.isError
            textRes = R.string.schedule_loading_error
            onRetryClick = { events.accept(Event.Retry) }
        }

        recyclerView.isVisible = !vm.isLoading && !vm.isError
        progressBar.isGone = !vm.isLoading || vm.isError

        itemAdapter.clear()
        val items = Section(
            ScheduleInfoHeaderItem(
                group = vm.schedule?.groupTitle ?: "",
                semester = vm.schedule?.semester ?: "",
                isWeekOdd = vm.currentWeek == 0
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
        if (itemAdapter.itemCount != 0 && headerIndices.isNotEmpty() && vm.currentDay >= 0) {
            val smoothScroller = object : LinearSmoothScroller(androidView.context) {
                override fun getVerticalSnapPreference() = SNAP_TO_START
            }
            smoothScroller.targetPosition = headerIndices[vm.currentDay]
            recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun openPopupMenu(
        classItem: ClassItem,
        view: View,
        classIndex: Int,
        dayIndex: Int,
        weekIndex: Int
    ) = PopupMenu(view.context, view).run {
        menuInflater.inflate(R.menu.class_entry_menu, menu)
        setOnMenuItemClickListener {
            events.accept(Event.ChangeClassVisibility(classIndex, dayIndex, weekIndex))
            true
        }
        show()
        menu.findItem(R.id.hide_class).isVisible = !classItem.hidden
        menu.findItem(R.id.restore_class).isVisible = classItem.hidden
    }
}
