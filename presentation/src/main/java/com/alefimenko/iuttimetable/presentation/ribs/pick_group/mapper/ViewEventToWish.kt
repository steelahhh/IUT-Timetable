package com.alefimenko.iuttimetable.presentation.ribs.pick_group.mapper

import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupView.Event
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? =
        TODO("Implement PickGroupViewEventToWish mapping")
}
