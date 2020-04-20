package com.alefimenko.iuttimetable.groups.builder

import com.alefimenko.iuttimetable.groups.Groups
import com.alefimenko.iuttimetable.groups.GroupsNode
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class GroupsBuilder(
    dependency: Groups.Dependency
) : SimpleBuilder<Groups.Dependency, GroupsNode>(
    rib = object : Groups {}
) {

    override val dependency: Groups.Dependency = object : Groups.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Groups::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): GroupsNode = DaggerGroupsComponent
        .factory()
        .create(
            dependency = dependency,
            customisation = dependency.getOrDefault(Groups.Customisation()),
            buildParams = buildParams
        )
        .node()
}
