package com.alefimenko.iuttimetable.schedule.mapper

import com.alefimenko.iuttimetable.schedule.ScheduleView.Event
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? = when (event) {
        Event.ChangeWeek -> Wish.RequestWeekChange
        is Event.ChangeClassVisibility -> Wish.ChangeClassVisibility(event.classIndex, event.dayIndex, event.weekIndex)
        is Event.SwitchToWeek -> Wish.UpdateCurrentWeek(event.weekIdx)
        Event.OnSettingsClick -> Wish.RouteToSettings
        Event.Retry -> Wish.RequestDownload
        is Event.OnMenuClick -> Wish.RouteToGroupPicker
    }
}
