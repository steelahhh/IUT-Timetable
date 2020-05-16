package com.alefimenko.iuttimetable.root.builder

import com.alefimenko.iuttimetable.root.Root
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class RootBuilder(
    private val dependency: Root.Dependency
) : SimpleBuilder<Root>() {

    override fun build(buildParams: BuildParams<Nothing?>): Root = object : Root {
        override val node: Node<*> = DaggerRootComponent
            .factory()
            .create(
                dependency = dependency,
                buildParams = buildParams
            )
            .node()
    }
}
