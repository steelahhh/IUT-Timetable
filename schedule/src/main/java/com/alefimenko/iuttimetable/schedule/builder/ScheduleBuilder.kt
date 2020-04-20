package com.alefimenko.iuttimetable.schedule.builder

import com.alefimenko.iuttimetable.schedule.Schedule
import com.alefimenko.iuttimetable.schedule.ScheduleNode
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class ScheduleBuilder(
    dependency: Schedule.Dependency
) : SimpleBuilder<Schedule.Dependency, ScheduleNode>(
    rib = object : Schedule {}
) {

    override val dependency: Schedule.Dependency = object : Schedule.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Schedule::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): ScheduleNode =
        DaggerScheduleComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Schedule.Customisation()),
                buildParams = buildParams
            )
            .node()
}
