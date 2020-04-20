package com.alefimenko.iuttimetable.pick_group_root.builder

import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.pick_group.PickGroup
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRoot
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRootNode
import com.alefimenko.iuttimetable.pick_institute.PickInstitute
import com.badoo.ribs.core.builder.BuildParams
import dagger.BindsInstance
import javax.inject.Named

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
            @BindsInstance buildParams: BuildParams<Boolean>,
            @BindsInstance @Named(Constants.PICK_GROUP_ROOT)
            isRoot: Boolean
        ): PickGroupRootComponent
    }

    fun node(): PickGroupRootNode
}
