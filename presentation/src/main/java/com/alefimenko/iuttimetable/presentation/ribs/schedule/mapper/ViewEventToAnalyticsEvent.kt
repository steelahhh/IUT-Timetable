package com.alefimenko.iuttimetable.presentation.ribs.schedule.mapper

import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleView.Event
import com.alefimenko.iuttimetable.presentation.ribs.schedule.analytics.ScheduleAnalytics
import com.alefimenko.iuttimetable.presentation.ribs.schedule.analytics.ScheduleAnalytics.Event.ViewEvent

internal object ViewEventToAnalyticsEvent : (Event) -> ScheduleAnalytics.Event? {

    override fun invoke(event: Event): ScheduleAnalytics.Event? = ViewEvent(event)
}
