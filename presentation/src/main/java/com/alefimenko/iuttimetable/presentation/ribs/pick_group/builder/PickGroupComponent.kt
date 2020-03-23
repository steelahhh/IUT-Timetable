package com.alefimenko.iuttimetable.presentation.ribs.pick_group.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupNode
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
            @BindsInstance savedInstanceState: Bundle?
        ): PickGroupComponent
    }

    fun node(): PickGroupNode
}
