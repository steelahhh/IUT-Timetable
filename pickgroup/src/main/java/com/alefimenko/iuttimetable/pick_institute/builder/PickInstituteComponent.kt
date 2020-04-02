package com.alefimenko.iuttimetable.pick_institute.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.pick_institute.PickInstitute
import com.alefimenko.iuttimetable.pick_institute.PickInstituteNode
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
            @BindsInstance savedInstanceState: Bundle?
        ): PickInstituteComponent
    }

    fun node(): PickInstituteNode
}
