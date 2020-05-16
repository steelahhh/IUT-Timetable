@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.settings.builder

import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.settings.Settings
import com.alefimenko.iuttimetable.settings.Settings.Output
import com.alefimenko.iuttimetable.settings.SettingsInteractor
import com.alefimenko.iuttimetable.settings.SettingsNode
import com.alefimenko.iuttimetable.settings.SettingsRouter
import com.alefimenko.iuttimetable.settings.feature.SettingsFeature
import com.badoo.ribs.core.builder.BuildParams
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module(includes = [DataModule::class])
internal object SettingsModule {

    @SettingsScope
    @Provides
    @JvmStatic
    internal fun router(
        component: SettingsComponent,
        buildParams: BuildParams<Nothing?>
    ): SettingsRouter = SettingsRouter(
        buildParams = buildParams,
        transitionHandler = null
    )

    @SettingsScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: SettingsRouter,
        output: Consumer<Output>,
        feature: SettingsFeature
    ): SettingsInteractor = SettingsInteractor(
        buildParams = buildParams,
        router = router,
        output = output,
        feature = feature
    )

    @SettingsScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: Settings.Customisation,
        router: SettingsRouter,
        interactor: SettingsInteractor,
        output: Consumer<Output>,
        feature: SettingsFeature
    ): SettingsNode = SettingsNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor,
        output = output,
        feature = feature
    )
}
