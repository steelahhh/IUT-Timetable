package com.alefimenko.iuttimetable.presentation.ribs.settings.mapper

import com.alefimenko.iuttimetable.presentation.ribs.settings.SettingsView.Event
import com.alefimenko.iuttimetable.presentation.ribs.settings.analytics.SettingsAnalytics
import com.alefimenko.iuttimetable.presentation.ribs.settings.analytics.SettingsAnalytics.Event.ViewEvent

internal object ViewEventToAnalyticsEvent : (Event) -> SettingsAnalytics.Event? {

    override fun invoke(event: Event): SettingsAnalytics.Event? = ViewEvent(event)
}
