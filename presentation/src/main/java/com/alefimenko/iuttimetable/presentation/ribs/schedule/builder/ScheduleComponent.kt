package com.alefimenko.iuttimetable.presentation.ribs.schedule.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.groups.Groups
import com.alefimenko.iuttimetable.presentation.ribs.schedule.Schedule
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleNode
import com.alefimenko.iuttimetable.presentation.ribs.settings.Settings
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
            @BindsInstance savedInstanceState: Bundle?
        ): ScheduleComponent
    }

    fun node(): ScheduleNode
}
