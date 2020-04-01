package com.alefimenko.iuttimetable.schedule.mapper

import com.alefimenko.iuttimetable.schedule.ScheduleView.ViewModel
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel = ViewModel(
        isLoading = state.isLoading,
        isError = state.isError,
        schedule = state.schedule,
        currentDay = state.currentDay,
        currentWeek = state.currentWeek,
        selectedWeek = state.selectedWeek
    )
}
