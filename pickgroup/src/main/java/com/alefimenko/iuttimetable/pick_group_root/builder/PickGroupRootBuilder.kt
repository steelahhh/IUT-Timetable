package com.alefimenko.iuttimetable.pick_group_root.builder

import com.alefimenko.iuttimetable.pick_group_root.PickGroupRoot
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder

class PickGroupRootBuilder(
    private val dependency: PickGroupRoot.Dependency
) : Builder<Boolean, PickGroupRoot>() {
    override fun build(buildParams: BuildParams<Boolean>): PickGroupRoot = object : PickGroupRoot {
        override val node: Node<*> = DaggerPickGroupRootComponent
            .factory()
            .create(
                dependency = dependency,
                buildParams = buildParams,
                isRoot = buildParams.payload
            )
            .node()
    }
}
