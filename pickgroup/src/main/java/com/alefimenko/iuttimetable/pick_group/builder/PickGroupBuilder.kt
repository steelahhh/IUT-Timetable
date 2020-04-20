package com.alefimenko.iuttimetable.pick_group.builder

import com.alefimenko.iuttimetable.pick_group.PickGroup
import com.alefimenko.iuttimetable.pick_group.PickGroupNode
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class PickGroupBuilder(
    override val dependency: PickGroup.Dependency
) : SimpleBuilder<PickGroup.Dependency, PickGroupNode>(
    rib = object : PickGroup {}
) {

    override fun build(buildParams: BuildParams<Nothing?>): PickGroupNode =
        DaggerPickGroupComponent
            .factory()
            .create(
                dependency = dependency,
                buildParams = buildParams
            )
            .node()
}
