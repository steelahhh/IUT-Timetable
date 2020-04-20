package com.alefimenko.iuttimetable.pick_institute.builder

import com.alefimenko.iuttimetable.pick_institute.PickInstitute
import com.alefimenko.iuttimetable.pick_institute.PickInstituteNode
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class PickInstituteBuilder(
    override val dependency: PickInstitute.Dependency
) : SimpleBuilder<PickInstitute.Dependency, PickInstituteNode>(
    rib = object : PickInstitute {}
) {

    override fun build(buildParams: BuildParams<Nothing?>): PickInstituteNode = DaggerPickInstituteComponent
        .factory()
        .create(
            dependency = dependency,
            buildParams = buildParams
        )
        .node()
}
