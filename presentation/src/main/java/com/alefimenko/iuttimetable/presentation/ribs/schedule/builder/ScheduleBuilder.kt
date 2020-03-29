package com.alefimenko.iuttimetable.presentation.ribs.schedule.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.schedule.Schedule
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleNode
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class ScheduleBuilder(
    dependency: Schedule.Dependency
) : Builder<Schedule.Dependency>() {

    override val dependency: Schedule.Dependency = object : Schedule.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Schedule::class)
    }

    fun build(savedInstanceState: Bundle?): ScheduleNode =
        DaggerScheduleComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Schedule.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
