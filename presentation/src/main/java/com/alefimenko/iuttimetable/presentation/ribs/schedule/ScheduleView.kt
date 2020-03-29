package com.alefimenko.iuttimetable.presentation.ribs.schedule

import com.alefimenko.iuttimetable.data.remote.model.Schedule
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleView.Event
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleView.ViewModel
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface ScheduleView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        data class ChangeClassVisibility(val classIndex: Int, val dayIndex: Int, val weekIndex: Int) : Event()
        object ChangeWeek : Event()
    }

    data class ViewModel(
        val isLoading: Boolean,
        val isError: Boolean,
        val schedule: Schedule? = null,
        val currentWeek: Int,
        val selectedWeek: Int
    )

    interface Factory : ViewFactory<Nothing?, ScheduleView>
}
