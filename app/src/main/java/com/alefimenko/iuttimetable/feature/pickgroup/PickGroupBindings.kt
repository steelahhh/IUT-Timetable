package com.alefimenko.iuttimetable.feature.pickgroup

import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupFeature.State
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupFeature.UiEvent
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupFeature.ViewModel
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupFeature.Wish
import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.using

/*
 * Created by Alexander Efimenko on 2019-03-06.
 */

class PickGroupBindings(
    view: PickGroupController,
    val feature: PickGroupFeature
) : AndroidBindings<PickGroupController>(view) {
    override fun setup(view: PickGroupController) {
        binder.bind(feature to view using ViewModelTransformer())
        binder.bind(view to feature using UiEventTransformer())
    }
}

private class ViewModelTransformer : (State) -> ViewModel {
    override fun invoke(state: State): ViewModel =
        ViewModel(
            groups = state.groups,
            group = state.group,
            isLoading = state.isLoading,
            isError = state.isError,
            isGroupPicked = state.group != null,
            isGroupsLoaded = state.groups.isNotEmpty()
        )
}

private class UiEventTransformer : (UiEvent) -> Wish? {
    override fun invoke(event: UiEvent): Wish = when (event) {
        is UiEvent.LoadGroupsClicked -> Wish.LoadGroups(
            event.form,
            event.instituteId
        )
        is UiEvent.GroupClicked -> Wish.SelectGroup(event.groupInfo)
    }
}
