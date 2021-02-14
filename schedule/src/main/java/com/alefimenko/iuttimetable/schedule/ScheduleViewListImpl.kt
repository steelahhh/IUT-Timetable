package com.alefimenko.iuttimetable.schedule

import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.schedule.ScheduleView.Event
import com.alefimenko.iuttimetable.schedule.data.model.EmptyDayItem
import com.alefimenko.iuttimetable.schedule.data.model.HeaderItem
import com.alefimenko.iuttimetable.schedule.data.model.Position
import com.alefimenko.iuttimetable.schedule.data.model.ScheduleInfoHeaderItem
import com.alefimenko.iuttimetable.schedule.data.model.toClassItem
import com.alefimenko.iuttimetable.schedule.databinding.RibScheduleListBinding
import com.jakewharton.rxrelay2.PublishRelay
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

/*
 * Author: steelahhh
 * 29/3/20
 */

internal class ScheduleViewListImpl(
    override val androidView: ViewGroup,
    override val events: PublishRelay<Event> = PublishRelay.create()
) : ScheduleView,
    ObservableSource<Event> by events,
    Consumer<ScheduleView.ViewModel> {

    private val itemAdapter = GroupAdapter<GroupieViewHolder>()
    private val headerIndices = mutableListOf<Int>()
    private val binding = RibScheduleListBinding.bind(androidView)

    init {
        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = itemAdapter
        }
        binding.toolbar.run {
            replaceMenu(R.menu.schedule_menu)
            setNavigationOnClickListener { events.accept(Event.OnMenuClick) }
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.action_settings) events.accept(Event.OnSettingsClick)
                true
            }
            changeMenuColors()
        }

        binding.scheduleChangeWeekButton.setOnClickListener {
            events.accept(Event.ChangeWeek)
        }
    }

    override fun accept(vm: ScheduleView.ViewModel) = with(binding) {
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
}
