package com.alefimenko.iuttimetable.pick_group.builder

import com.alefimenko.iuttimetable.pick_group.PickGroup
import com.alefimenko.iuttimetable.pick_group.PickGroupNode
import com.badoo.ribs.core.builder.BuildParams
import dagger.BindsInstance

@PickGroupScope
@dagger.Component(
    modules = [PickGroupModule::class],
    dependencies = [PickGroup.Dependency::class]
)
internal interface PickGroupComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: PickGroup.Dependency,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): PickGroupComponent
    }

    fun node(): PickGroupNode
}
