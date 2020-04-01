package com.alefimenko.iuttimetable.pick_group.mapper

import com.alefimenko.iuttimetable.pick_group.PickGroup.Input
import com.alefimenko.iuttimetable.pick_group.feature.PickGroupFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? = when (event) {
        is Input.GroupInfoReceived -> Wish.LoadGroups(event.form, event.institute)
    }
}
