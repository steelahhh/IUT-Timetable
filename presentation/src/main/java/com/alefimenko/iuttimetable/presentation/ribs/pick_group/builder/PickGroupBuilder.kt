package com.alefimenko.iuttimetable.presentation.ribs.pick_group.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupNode
import com.badoo.ribs.core.Builder

class PickGroupBuilder(
    override val dependency: PickGroup.Dependency
) : Builder<PickGroup.Dependency>() {

    fun build(savedInstanceState: Bundle?): PickGroupNode =
        DaggerPickGroupComponent
            .factory()
            .create(
                dependency = dependency,
                savedInstanceState = savedInstanceState
            )
            .node()
}
