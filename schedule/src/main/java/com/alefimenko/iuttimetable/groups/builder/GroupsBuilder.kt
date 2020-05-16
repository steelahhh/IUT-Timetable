package com.alefimenko.iuttimetable.groups.builder

import com.alefimenko.iuttimetable.groups.Groups
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class GroupsBuilder(
    private val dependency: Groups.Dependency
) : SimpleBuilder<Groups>() {

    override fun build(buildParams: BuildParams<Nothing?>): Groups = object : Groups {
        override val node: Node<*> = DaggerGroupsComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(Groups.Customisation()),
                buildParams = buildParams
            )
            .node()
    }
}
