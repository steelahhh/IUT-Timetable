package com.alefimenko.iuttimetable.presentation.ribs.pick_group.mapper

import com.alefimenko.iuttimetable.presentation.data.GroupItem
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupView.ViewModel
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel = ViewModel(
        isLoading = state.isLoading,
        isError = state.isError,
        groups = state.filteredGroups.map { group ->
            GroupItem(
                group.id,
                group.name
            )
        }
    )
}
