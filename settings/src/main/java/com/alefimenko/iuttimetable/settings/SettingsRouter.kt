package com.alefimenko.iuttimetable.settings

import android.os.Bundle
import android.os.Parcelable
import com.alefimenko.iuttimetable.settings.SettingsRouter.Configuration
import com.alefimenko.iuttimetable.settings.SettingsRouter.Configuration.Content
import com.alefimenko.iuttimetable.settings.SettingsRouter.Configuration.Overlay
import com.alefimenko.iuttimetable.settings.SettingsRouter.Configuration.Permanent
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class SettingsRouter(
    savedInstanceState: Bundle?,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration, Permanent, Content, Overlay, SettingsView>(
    savedInstanceState = savedInstanceState,
    transitionHandler = transitionHandler,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration()
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
        sealed class Overlay : Configuration()
    }

    override fun resolveConfiguration(
        configuration: Configuration
    ): RoutingAction<SettingsView> = RoutingAction.noop()
}
