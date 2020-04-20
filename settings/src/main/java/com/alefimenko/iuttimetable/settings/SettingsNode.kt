package com.alefimenko.iuttimetable.settings

import android.view.ViewGroup
import com.alefimenko.iuttimetable.settings.feature.SettingsFeature
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import io.reactivex.functions.Consumer

class SettingsNode internal constructor(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ((ViewGroup) -> SettingsView?)?,
    private val router: SettingsRouter,
    private val output: Consumer<Settings.Output>,
    private val feature: SettingsFeature,
    private val interactor: SettingsInteractor
) : Node<SettingsView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
)
