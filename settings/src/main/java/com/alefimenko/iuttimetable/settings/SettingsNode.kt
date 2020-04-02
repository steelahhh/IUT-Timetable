package com.alefimenko.iuttimetable.settings

import android.os.Bundle
import android.view.ViewGroup
import com.alefimenko.iuttimetable.settings.feature.SettingsFeature
import com.badoo.ribs.core.Node
import io.reactivex.functions.Consumer

class SettingsNode internal constructor(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> SettingsView?)?,
    private val router: SettingsRouter,
    private val output: Consumer<Settings.Output>,
    private val feature: SettingsFeature,
    private val interactor: SettingsInteractor
) : Node<SettingsView>(
    savedInstanceState = savedInstanceState,
    identifier = object : Settings {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
)
