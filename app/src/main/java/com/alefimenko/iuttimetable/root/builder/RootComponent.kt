package com.alefimenko.iuttimetable.root.builder

import android.content.Context
import android.os.Bundle
import com.alefimenko.iuttimetable.root.Root
import com.alefimenko.iuttimetable.root.RootNode
import dagger.BindsInstance

@RootScope
@dagger.Component(
    modules = [RootModule::class],
    dependencies = [Root.Dependency::class]
)
internal interface RootComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Root.Dependency,
            @BindsInstance savedInstanceState: Bundle?
        ): RootComponent
    }

    fun context(): Context

    fun node(): RootNode
}
