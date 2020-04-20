package com.alefimenko.iuttimetable.pick_group_root.builder

import com.alefimenko.iuttimetable.pick_group_root.PickGroupRoot
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRootNode
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder

class PickGroupRootBuilder(
    override val dependency: PickGroupRoot.Dependency
) : Builder<PickGroupRoot.Dependency, Boolean, PickGroupRootNode>(
    rib = object : PickGroupRoot {}
) {
    override fun build(buildParams: BuildParams<Boolean>): PickGroupRootNode = DaggerPickGroupRootComponent
        .factory()
        .create(
            dependency = dependency,
            buildParams = buildParams,
            isRoot = buildParams.payload
        )
        .node()
}
