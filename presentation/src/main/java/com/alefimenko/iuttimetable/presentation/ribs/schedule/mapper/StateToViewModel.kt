package com.alefimenko.iuttimetable.presentation.ribs.schedule.mapper

import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleView.ViewModel
import com.alefimenko.iuttimetable.presentation.ribs.schedule.feature.ScheduleFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel = ViewModel(
        isLoading = state.isLoading,
        isError = state.isError,
        schedule = state.schedule,
        currentWeek = state.currentWeek,
        selectedWeek = state.selectedWeek
    )
}
