package com.alefimenko.iuttimetable.schedule.mapper

import com.alefimenko.iuttimetable.schedule.ScheduleView.Event
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? = when (event) {
        is Event.ChangeWeek -> Wish.RequestWeekChange
        is Event.ChangeClassVisibility -> Wish.ChangeClassVisibility(event.classIndex, event.dayIndex, event.weekIndex)
        is Event.SwitchToWeek -> Wish.UpdateCurrentWeek(event.weekIdx)
        is Event.OnSettingsClick -> Wish.RouteToSettings
        is Event.Retry -> Wish.RequestDownload
        is Event.OnMenuClick -> Wish.RouteToGroupPicker
        is Event.HideAll -> Wish.HideAll(event.title)
        is Event.RestoreAll -> Wish.RestoreAll(event.title)
    }
}
