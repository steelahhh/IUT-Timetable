package com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRoot
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRootNode
import com.badoo.ribs.core.Builder

class PickGroupRootBuilder(
    override val dependency: PickGroupRoot.Dependency
) : Builder<PickGroupRoot.Dependency>() {
    fun build(savedInstanceState: Bundle?): PickGroupRootNode =
        DaggerPickGroupRootComponent
            .factory()
            .create(
                dependency = dependency,
                savedInstanceState = savedInstanceState
            )
            .node()
}
