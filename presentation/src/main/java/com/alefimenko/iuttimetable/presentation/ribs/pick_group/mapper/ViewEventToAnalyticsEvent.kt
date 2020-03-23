package com.alefimenko.iuttimetable.presentation.ribs.pick_group.mapper

import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupView.Event
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.analytics.PickGroupAnalytics
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.analytics.PickGroupAnalytics.Event.ViewEvent

internal object ViewEventToAnalyticsEvent : (Event) -> PickGroupAnalytics.Event? {

    override fun invoke(event: Event): PickGroupAnalytics.Event? =
        ViewEvent(event)
}
