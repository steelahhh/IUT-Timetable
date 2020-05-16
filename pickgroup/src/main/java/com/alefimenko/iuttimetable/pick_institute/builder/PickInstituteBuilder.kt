package com.alefimenko.iuttimetable.pick_institute.builder

import com.alefimenko.iuttimetable.pick_institute.PickInstitute
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class PickInstituteBuilder(
    private val dependency: PickInstitute.Dependency
) : SimpleBuilder<PickInstitute>() {

    override fun build(buildParams: BuildParams<Nothing?>): PickInstitute =
        object : PickInstitute {
            override val node: Node<*> = DaggerPickInstituteComponent
                .factory()
                .create(
                    dependency = dependency,
                    buildParams = buildParams
                )
                .node()
        }
}
