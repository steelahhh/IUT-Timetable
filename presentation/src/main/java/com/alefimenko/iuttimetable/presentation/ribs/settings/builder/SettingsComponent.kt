package com.alefimenko.iuttimetable.presentation.ribs.settings.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.settings.Settings
import com.alefimenko.iuttimetable.presentation.ribs.settings.SettingsNode
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
            @BindsInstance savedInstanceState: Bundle?
        ): SettingsComponent
    }

    fun node(): SettingsNode
}
