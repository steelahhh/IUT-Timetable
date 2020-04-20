package com.alefimenko.iuttimetable.root.builder

import com.alefimenko.iuttimetable.root.Root
import com.alefimenko.iuttimetable.root.RootNode
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class RootBuilder(
    override val dependency: Root.Dependency
) : SimpleBuilder<Root.Dependency, RootNode>(
    rib = object : Rib {}
) {

    override fun build(buildParams: BuildParams<Nothing?>): RootNode = DaggerRootComponent
        .factory()
        .create(
            dependency = dependency,
            buildParams = buildParams
        )
        .node()
}
