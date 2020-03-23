package com.alefimenko.iuttimetable.presentation.ribs.pick_group.mapper

import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup.Input
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? =
        TODO("Implement PickGroupInputToWish mapping")
}
