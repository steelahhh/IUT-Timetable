package com.alefimenko.iuttimetable.pick_institute.builder

import com.alefimenko.iuttimetable.pick_institute.PickInstitute
import com.alefimenko.iuttimetable.pick_institute.PickInstituteNode
import com.badoo.ribs.core.builder.BuildParams
import dagger.BindsInstance

@PickInstituteScope
@dagger.Component(
    modules = [PickInstituteModule::class],
    dependencies = [PickInstitute.Dependency::class]
)
internal interface PickInstituteComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: PickInstitute.Dependency,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): PickInstituteComponent
    }

    fun node(): PickInstituteNode
}
