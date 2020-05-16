package com.alefimenko.iuttimetable.settings.builder

import com.alefimenko.iuttimetable.settings.Settings
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder

class SettingsBuilder(
    private val dependency: Settings.Dependency
) : SimpleBuilder<Settings>() {
    override fun build(buildParams: BuildParams<Nothing?>): Settings {
        return object : Settings {
            override val node: Node<*> = DaggerSettingsComponent
                .factory()
                .create(
                    dependency = dependency,
                    customisation = buildParams.getOrDefault(Settings.Customisation()),
                    buildParams = buildParams
                )
                .node()
        }
    }
}
