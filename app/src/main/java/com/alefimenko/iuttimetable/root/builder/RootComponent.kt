package com.alefimenko.iuttimetable.root.builder

import android.content.Context
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRoot
import com.alefimenko.iuttimetable.root.Root
import com.alefimenko.iuttimetable.root.RootNode
import com.alefimenko.iuttimetable.schedule.Schedule
import com.badoo.ribs.core.builder.BuildParams
import dagger.BindsInstance

@RootScope
@dagger.Component(
    modules = [RootModule::class],
    dependencies = [Root.Dependency::class]
)
internal interface RootComponent : PickGroupRoot.Dependency, Schedule.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Root.Dependency,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): RootComponent
    }

    fun context(): Context

    fun node(): RootNode
}
