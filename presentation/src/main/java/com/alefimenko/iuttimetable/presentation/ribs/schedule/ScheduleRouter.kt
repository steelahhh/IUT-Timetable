package com.alefimenko.iuttimetable.presentation.ribs.schedule

import android.os.Bundle
import android.os.Parcelable
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleRouter.Configuration
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleRouter.Configuration.Content
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleRouter.Configuration.Overlay
import com.alefimenko.iuttimetable.presentation.ribs.settings.builder.SettingsBuilder
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class ScheduleRouter(
    private val settingsBuilder: SettingsBuilder,
    savedInstanceState: Bundle?,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration, Nothing, Content, Overlay, ScheduleView>(
    savedInstanceState = savedInstanceState,
    transitionHandler = transitionHandler,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
            @Parcelize object Settings : Content()
        }
        sealed class Overlay : Configuration()
    }

    override fun resolveConfiguration(
        configuration: Configuration
    ): RoutingAction<ScheduleView> = when (configuration) {
        is Content.Settings -> attach { settingsBuilder.build(it) }
        else -> RoutingAction.noop()
    }
}
