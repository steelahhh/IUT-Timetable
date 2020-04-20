package com.alefimenko.iuttimetable.settings.builder

import com.alefimenko.iuttimetable.settings.Settings
import com.alefimenko.iuttimetable.settings.SettingsNode
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class SettingsBuilder(
    dependency: Settings.Dependency
) : SimpleBuilder<Settings.Dependency, SettingsNode>(
    rib = object : Settings {}
) {

    override val dependency: Settings.Dependency = object : Settings.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Settings::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): SettingsNode =
        DaggerSettingsComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Settings.Customisation()),
                buildParams = buildParams
            )
            .node()
}
