package com.alefimenko.iuttimetable.pick_institute.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.pick_institute.PickInstitute
import com.alefimenko.iuttimetable.pick_institute.PickInstituteNode
import com.badoo.ribs.core.Builder

class PickInstituteBuilder(
    override val dependency: PickInstitute.Dependency
) : Builder<PickInstitute.Dependency>() {

    fun build(savedInstanceState: Bundle?): PickInstituteNode =
        DaggerPickInstituteComponent
            .factory()
            .create(
                dependency = dependency,
                savedInstanceState = savedInstanceState
            )
            .node()
}
