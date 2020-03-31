package com.alefimenko.iuttimetable.presentation.ribs.groups.mapper

import com.alefimenko.iuttimetable.presentation.ribs.groups.Groups.Input
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? = null
}
