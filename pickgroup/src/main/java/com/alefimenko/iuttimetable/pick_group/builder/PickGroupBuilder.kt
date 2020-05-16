package com.alefimenko.iuttimetable.pick_group.builder

import com.alefimenko.iuttimetable.pick_group.PickGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class PickGroupBuilder(
    val dependency: PickGroup.Dependency
) : SimpleBuilder<PickGroup>() {
    override fun build(buildParams: BuildParams<Nothing?>): PickGroup =
        object : PickGroup {
            override val node: Node<*> = DaggerPickGroupComponent
                .factory()
                .create(
                    dependency = dependency,
                    buildParams = buildParams
                )
                .node()
        }
}
