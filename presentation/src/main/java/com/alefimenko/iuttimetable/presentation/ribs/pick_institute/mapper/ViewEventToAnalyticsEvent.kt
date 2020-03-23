package com.alefimenko.iuttimetable.presentation.ribs.pick_institute.mapper

import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstituteView.Event
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.analytics.PickInstituteAnalytics
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.analytics.PickInstituteAnalytics.Event.ViewEvent

internal object ViewEventToAnalyticsEvent : (Event) -> PickInstituteAnalytics.Event? {

    override fun invoke(event: Event): PickInstituteAnalytics.Event? =
        ViewEvent(event)
}
