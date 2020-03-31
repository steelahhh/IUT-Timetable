package com.alefimenko.iuttimetable.presentation.ribs.groups.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.groups.Groups
import com.alefimenko.iuttimetable.presentation.ribs.groups.GroupsNode
import dagger.BindsInstance

@GroupsScope
@dagger.Component(
    modules = [GroupsModule::class],
    dependencies = [Groups.Dependency::class]
)
internal interface GroupsComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Groups.Dependency,
            @BindsInstance customisation: Groups.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): GroupsComponent
    }

    fun node(): GroupsNode
}
