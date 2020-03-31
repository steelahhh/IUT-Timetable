package com.alefimenko.iuttimetable.presentation.ribs.groups.mapper

import com.alefimenko.iuttimetable.presentation.data.GroupUi
import com.alefimenko.iuttimetable.presentation.ribs.groups.GroupsView.ViewModel
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel = ViewModel(
        groups = state.groups.map { (group, institutes) ->
            GroupUi(
                id = group.id,
                name = group.name,
                instituteName = institutes.first().name,
                semester = group.semester,
                isCurrent = group.id == state.currentGroup
            )
        }.sortedByDescending { it.isCurrent }
    )
}
