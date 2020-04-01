package com.alefimenko.iuttimetable.pick_group.mapper

import com.alefimenko.iuttimetable.data.Group
import com.alefimenko.iuttimetable.pick_group.PickGroupView.Event
import com.alefimenko.iuttimetable.pick_group.feature.PickGroupFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {
    override fun invoke(event: Event): Wish? = when (event) {
        Event.GoBack -> Wish.GoBack
        is Event.PickGroup -> Wish.PickGroup(Group(event.group.id, event.group.label))
        is Event.QueryChanged -> Wish.QueryChanged(event.query)
        Event.Retry -> Wish.Retry
    }
}
