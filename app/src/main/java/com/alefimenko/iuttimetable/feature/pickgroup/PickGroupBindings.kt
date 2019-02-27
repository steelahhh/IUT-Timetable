package com.alefimenko.iuttimetable.feature.pickgroup

import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.using

/*
 * Created by Alexander Efimenko on 2019-02-22.
 */

class PickGroupBindings(
    view: PickInstituteFragment,
    val feature: PickGroupFeature
) : AndroidBindings<PickInstituteFragment>(view) {
    override fun setup(view: PickInstituteFragment) {
        binder.bind(feature to view using ViewModelTransformer())
        binder.bind(view to feature using UiEventTransformer())
    }
}

private class ViewModelTransformer : (PickGroupFeature.State) -> PickGroupFeature.ViewModel {
    override fun invoke(state: PickGroupFeature.State) = PickGroupFeature.ViewModel(
        institutes = state.institutes,
        institute = state.institute,
        form = state.form,
        isLoading = state.isLoading
    )
}

private class UiEventTransformer : (PickGroupFeature.UiEvent) -> PickGroupFeature.Wish? {
    override fun invoke(event: PickGroupFeature.UiEvent): PickGroupFeature.Wish? = when (event) {
        is PickGroupFeature.UiEvent.NextButtonClicked -> PickGroupFeature.Wish.NavigateToPickGroup
        is PickGroupFeature.UiEvent.LoadInstitutesClicked -> PickGroupFeature.Wish.LoadInstitutes
        is PickGroupFeature.UiEvent.FormClicked -> PickGroupFeature.Wish.SelectForm(event.id)
        is PickGroupFeature.UiEvent.InstituteClicked -> PickGroupFeature.Wish.SelectInstitute(event.institute)
    }
}
