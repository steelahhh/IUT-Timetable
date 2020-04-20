package com.alefimenko.iuttimetable.schedule.builder

import com.alefimenko.iuttimetable.groups.Groups
import com.alefimenko.iuttimetable.schedule.Schedule
import com.alefimenko.iuttimetable.schedule.ScheduleNode
import com.alefimenko.iuttimetable.settings.Settings
import com.badoo.ribs.core.builder.BuildParams
import dagger.BindsInstance

@ScheduleScope
@dagger.Component(
    modules = [ScheduleModule::class],
    dependencies = [Schedule.Dependency::class]
)
internal interface ScheduleComponent : Settings.Dependency, Groups.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Schedule.Dependency,
            @BindsInstance customisation: Schedule.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): ScheduleComponent
    }

    fun node(): ScheduleNode
}
