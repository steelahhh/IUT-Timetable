package com.alefimenko.iuttimetable.presentation.ribs.pick_institute.analytics

import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstituteView
import io.reactivex.functions.Consumer

internal object PickInstituteAnalytics : Consumer<PickInstituteAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: PickInstituteView.Event) : Event()
    }

    override fun accept(event: PickInstituteAnalytics.Event) {
        // TODO Implement tracking
    }
}
