package com.alefimenko.iuttimetable.groups.mapper

import com.alefimenko.iuttimetable.groups.Groups.Input
import com.alefimenko.iuttimetable.groups.feature.GroupsFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? = null
}
