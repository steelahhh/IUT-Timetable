package com.alefimenko.iuttimetable.presentation.ribs.settings.analytics

import com.alefimenko.iuttimetable.presentation.ribs.settings.SettingsView
import io.reactivex.functions.Consumer

internal object SettingsAnalytics : Consumer<SettingsAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: SettingsView.Event) : Event()
    }

    override fun accept(event: SettingsAnalytics.Event) {
        // TODO Implement tracking
    }
}
