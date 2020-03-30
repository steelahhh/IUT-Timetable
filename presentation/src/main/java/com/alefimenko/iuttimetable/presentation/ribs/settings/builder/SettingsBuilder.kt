package com.alefimenko.iuttimetable.presentation.ribs.settings.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.settings.Settings
import com.alefimenko.iuttimetable.presentation.ribs.settings.SettingsNode
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class SettingsBuilder(
    dependency: Settings.Dependency
) : Builder<Settings.Dependency>() {

    override val dependency: Settings.Dependency = object : Settings.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Settings::class)
    }

    fun build(savedInstanceState: Bundle?): SettingsNode =
        DaggerSettingsComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Settings.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
