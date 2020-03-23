package com.alefimenko.iuttimetable.presentation.ribs.pick_group.analytics

import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupView
import io.reactivex.functions.Consumer

internal object PickGroupAnalytics : Consumer<PickGroupAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: PickGroupView.Event) : Event()
    }

    override fun accept(event: PickGroupAnalytics.Event) {
        // TODO Implement tracking
    }
}
