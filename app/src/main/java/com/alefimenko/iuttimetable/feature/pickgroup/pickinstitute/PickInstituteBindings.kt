package com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute

import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature.State
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature.UiEvent
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature.ViewModel
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature.Wish
import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.using

/*
 * Created by Alexander Efimenko on 2019-02-22.
 */

class PickInstituteBindings(
    view: PickInstituteController,
    val feature: PickInstituteFeature
) : AndroidBindings<PickInstituteController>(view) {
    override fun setup(view: PickInstituteController) {
        binder.bind(feature to view using ViewModelTransformer())
        binder.bind(view to feature using UiEventTransformer())
    }
}

private class ViewModelTransformer : (State) -> ViewModel {
    override fun invoke(state: State) =
        ViewModel(
            institutes = state.institutes,
            institute = state.institute,
            form = state.form,
            isLoading = state.isLoading,
            isError = state.isError,
            isInstitutePicked = state.institute != null,
            isInstitutesLoaded = state.institutes.isNotEmpty()
        )
}

private class UiEventTransformer : (UiEvent) -> PickInstituteFeature.Wish? {
    override fun invoke(event: UiEvent): PickInstituteFeature.Wish? = when (event) {
        is UiEvent.NextButtonClicked -> Wish.NavigateToPickGroup
        is UiEvent.LoadInstitutesClicked -> Wish.LoadInstitutes
        is UiEvent.FormClicked -> Wish.SelectForm(
            event.id
        )
        is UiEvent.InstituteClicked -> Wish.SelectInstitute(
            event.institute
        )
    }
}
