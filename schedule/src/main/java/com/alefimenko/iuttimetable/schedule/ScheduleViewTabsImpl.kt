package com.alefimenko.iuttimetable.schedule

import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.groups.Groups
import com.alefimenko.iuttimetable.schedule.ScheduleView.ViewModel
import com.alefimenko.iuttimetable.schedule.data.model.ClassItem
import com.alefimenko.iuttimetable.schedule.data.model.DayTabItem
import com.alefimenko.iuttimetable.schedule.data.model.Position
import com.alefimenko.iuttimetable.schedule.data.model.toClassItem
import com.alefimenko.iuttimetable.schedule.databinding.RibScheduleTabsBinding
import com.badoo.ribs.core.Node
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxrelay2.PublishRelay
import com.soywiz.klock.KlockLocale
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

internal class ScheduleViewTabsImpl(
    override val androidView: ViewGroup,
    private val events: PublishRelay<ScheduleView.Event> = PublishRelay.create()
) : ScheduleView,
    ObservableSource<ScheduleView.Event> by events,
    Consumer<ViewModel> {

    private val tabsAdapter = GroupAdapter<GroupieViewHolder>()
    private val binding = RibScheduleTabsBinding.bind(androidView)

    init {
        binding.scheduleTabsToolbar.run {
            replaceMenu(R.menu.schedule_menu)
            setNavigationOnClickListener { events.accept(ScheduleView.Event.OnMenuClick) }
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.action_settings) events.accept(ScheduleView.Event.OnSettingsClick)
                true
            }
            changeMenuColors()
        }

        binding.scheduleTabsViewPager.adapter = tabsAdapter

        binding.scheduleChangeWeekButton.setOnClickListener {
            events.accept(ScheduleView.Event.ChangeWeek)
        }
    }

    override fun openWeekPickerDialog(weeks: List<String>, selectedWeek: Int) {
        MaterialDialog(androidView.context).show {
            title(res = R.string.pick_week_dialog_title)
            listItemsSingleChoice(
                items = weeks,
                initialSelection = selectedWeek,
                selection = { _, index, _ ->
                    events.accept(ScheduleView.Event.SwitchToWeek(index))
                }
            )
        }
    }

    override fun accept(vm: ViewModel) = with(binding) {
        scheduleStubView.apply {
            isVisible = vm.isError
            textRes = R.string.schedule_loading_error
            onRetryClick = { events.accept(ScheduleView.Event.Retry) }
        }

        scheduleTabsViewPager.isVisible = !vm.isLoading && !vm.isError
        scheduleTabLayout.isVisible = !vm.isLoading && !vm.isError
        scheduleTabsAppbarLayout.isVisible = !vm.isLoading && !vm.isError
        progressBar.isGone = !vm.isLoading || vm.isError
        scheduleChangeWeekButton.isGone = vm.isError || vm.isLoading

        val classes = mutableListOf<Item<*>>()

        vm.schedule?.let { schedule ->
            binding.scheduleTabsHeaderLayout.run {
                scheduleHeaderTitle.text = schedule.groupTitle
                scheduleHeaderSubtitle.text = schedule.semester
            }

            TabLayoutMediator(binding.scheduleTabLayout, binding.scheduleTabsViewPager) { tab, position ->
                tab.text = KlockLocale.default.daysOfWeekShort[position]
            }.attach()

            schedule.weekSchedule[vm.selectedWeek]?.forEachIndexed { dayIndex, list ->
                classes.add(
                    DayTabItem(
                        list.mapIndexed { listIdx, classEntry ->
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
                        }
                    )
                )
            }
            scheduleChangeWeekButton.text = schedule.weeks[vm.selectedWeek]
        }
        tabsAdapter.clear()
        tabsAdapter.addAll(classes)
        // TODO: extract this into event and trigger it only on start?
        if (tabsAdapter.itemCount != 0 && vm.currentDay >= 0) {
            scheduleTabsViewPager.setCurrentItem(vm.currentDay, true)
        } else {
            scheduleTabsViewPager.currentItem = 0
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
            events.accept(ScheduleView.Event.ChangeClassVisibility(classIndex, dayIndex, weekIndex))
            true
        }
        show()
        menu.findItem(R.id.hide_class).isVisible = !classItem.hidden
        menu.findItem(R.id.restore_class).isVisible = classItem.hidden
    }
}
