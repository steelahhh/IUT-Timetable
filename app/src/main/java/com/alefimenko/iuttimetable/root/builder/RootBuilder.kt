package com.alefimenko.iuttimetable.root.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.root.Root
import com.alefimenko.iuttimetable.root.RootNode
import com.badoo.ribs.core.Builder

class RootBuilder(
    override val dependency: Root.Dependency
) : Builder<Root.Dependency>() {

    fun build(savedInstanceState: Bundle?): RootNode =
        DaggerRootComponent
            .factory()
            .create(
                dependency = dependency,
                savedInstanceState = savedInstanceState
            )
            .node()
}
