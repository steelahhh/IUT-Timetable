package com.alefimenko.iuttimetable.presentation.ribs.schedule.analytics

import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleView
import io.reactivex.functions.Consumer

internal object ScheduleAnalytics : Consumer<ScheduleAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: ScheduleView.Event) : Event()
    }

    override fun accept(event: ScheduleAnalytics.Event) {
        // TODO Implement tracking
    }
}
