package com.alefimenko.iuttimetable.presentation.ribs.groups.mapper

import com.alefimenko.iuttimetable.presentation.ribs.groups.GroupsView.Event
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? = when (event) {
        is Event.Dismiss -> Wish.GoBack
        is Event.OnGroupClicked -> Wish.SelectGroup(event.group)
        is Event.OnDeleteGroup -> Wish.DeleteGroup(event.group)
        is Event.OnAddGroupClick -> Wish.AddNewGroup
    }
}
