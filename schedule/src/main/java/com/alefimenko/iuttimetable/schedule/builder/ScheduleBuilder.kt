package com.alefimenko.iuttimetable.schedule.builder

import com.alefimenko.iuttimetable.schedule.Schedule
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class ScheduleBuilder(
    private val dependency: Schedule.Dependency
) : SimpleBuilder<Schedule>() {

    override fun build(buildParams: BuildParams<Nothing?>): Schedule = object : Schedule {
        override val node: Node<*> = DaggerScheduleComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(Schedule.Customisation()),
                buildParams = buildParams
            )
            .node()
    }
}
