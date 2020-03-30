package com.alefimenko.iuttimetable.presentation.ribs.settings

import com.alefimenko.iuttimetable.common.CanProvideContext
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation
import io.reactivex.functions.Consumer

interface Settings : Rib {

    interface Dependency : CanProvideRibCustomisation, CanProvideContext {
        fun settingsOutput(): Consumer<Output>
    }

    sealed class Output {
        object GoBack : Output()
    }

    class Customisation(
        val viewFactory: SettingsView.Factory = SettingsViewImpl.Factory(),
        val transitionHandler: TransitionHandler<SettingsRouter.Configuration>? = null
    ) : RibCustomisation
}
