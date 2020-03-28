package com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRoot
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRootNode
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstitute
import dagger.BindsInstance

@PickGroupRootScope
@dagger.Component(
    modules = [PickGroupRootModule::class],
    dependencies = [PickGroupRoot.Dependency::class]
)
internal interface PickGroupRootComponent : PickInstitute.Dependency, PickGroup.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: PickGroupRoot.Dependency,
            @BindsInstance savedInstanceState: Bundle?
        ): PickGroupRootComponent
    }

    fun node(): PickGroupRootNode
}
