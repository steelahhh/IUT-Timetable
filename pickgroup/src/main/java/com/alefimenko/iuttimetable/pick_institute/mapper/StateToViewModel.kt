package com.alefimenko.iuttimetable.pick_institute.mapper

import com.alefimenko.iuttimetable.pick_institute.PickInstituteView.ViewModel
import com.alefimenko.iuttimetable.pick_institute.feature.PickInstituteFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel = ViewModel(
        isLoading = state.isLoading,
        isError = state.isError,
        institutes = state.institutes,
        institute = state.institute
    )
}
