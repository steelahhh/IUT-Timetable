@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.settings.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.settings.Settings
import com.alefimenko.iuttimetable.settings.Settings.Output
import com.alefimenko.iuttimetable.settings.SettingsInteractor
import com.alefimenko.iuttimetable.settings.SettingsNode
import com.alefimenko.iuttimetable.settings.SettingsRouter
import com.alefimenko.iuttimetable.settings.feature.SettingsFeature
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module(includes = [DataModule::class])
internal object SettingsModule {

    @SettingsScope
    @Provides
    @JvmStatic
    internal fun router(
        component: SettingsComponent,
        savedInstanceState: Bundle?,
        customisation: Settings.Customisation
    ): SettingsRouter = SettingsRouter(
        savedInstanceState = savedInstanceState,
        transitionHandler = null
    )

    @SettingsScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: SettingsRouter,
        output: Consumer<Output>,
        feature: SettingsFeature
    ): SettingsInteractor = SettingsInteractor(
        savedInstanceState = savedInstanceState,
        router = router,
        output = output,
        feature = feature
    )

    @SettingsScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: Settings.Customisation,
        router: SettingsRouter,
        interactor: SettingsInteractor,
        output: Consumer<Output>,
        feature: SettingsFeature
    ): SettingsNode = SettingsNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor,
        output = output,
        feature = feature
    )
}
