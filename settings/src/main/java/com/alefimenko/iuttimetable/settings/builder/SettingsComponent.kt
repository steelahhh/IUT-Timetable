package com.alefimenko.iuttimetable.settings.builder

import com.alefimenko.iuttimetable.settings.Settings
import com.alefimenko.iuttimetable.settings.SettingsNode
import com.badoo.ribs.core.builder.BuildParams
import dagger.BindsInstance

@SettingsScope
@dagger.Component(
    modules = [SettingsModule::class],
    dependencies = [Settings.Dependency::class]
)
internal interface SettingsComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Settings.Dependency,
            @BindsInstance customisation: Settings.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): SettingsComponent
    }

    fun node(): SettingsNode
}
