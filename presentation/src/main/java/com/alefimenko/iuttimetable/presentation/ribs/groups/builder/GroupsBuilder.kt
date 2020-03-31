package com.alefimenko.iuttimetable.presentation.ribs.groups.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.groups.Groups
import com.alefimenko.iuttimetable.presentation.ribs.groups.GroupsNode
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class GroupsBuilder(
    dependency: Groups.Dependency
) : Builder<Groups.Dependency>() {

    override val dependency: Groups.Dependency = object : Groups.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Groups::class)
    }

    fun build(savedInstanceState: Bundle?): GroupsNode =
        DaggerGroupsComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Groups.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
