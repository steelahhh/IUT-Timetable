package com.alefimenko.iuttimetable.schedule

import android.os.Parcelable
import com.alefimenko.iuttimetable.groups.builder.GroupsBuilder
import com.alefimenko.iuttimetable.schedule.ScheduleRouter.Configuration
import com.alefimenko.iuttimetable.schedule.ScheduleRouter.Configuration.Content
import com.alefimenko.iuttimetable.schedule.ScheduleRouter.Configuration.Overlay
import com.alefimenko.iuttimetable.settings.builder.SettingsBuilder
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class ScheduleRouter(
    private val settingsBuilder: SettingsBuilder,
    private val groupsBuilder: GroupsBuilder,
    buildParams: BuildParams<Nothing?>,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration, Nothing, Content, Overlay, ScheduleView>(
    buildParams = buildParams,
    transitionHandler = transitionHandler,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
            @Parcelize object Settings : Content()
        }
        sealed class Overlay : Configuration() {
            @Parcelize object GroupsPicker : Overlay()
        }
    }

    override fun resolveConfiguration(
        configuration: Configuration
    ): RoutingAction = when (configuration) {
        is Content.Settings -> attach { settingsBuilder.build(it) }
        is Overlay.GroupsPicker -> attach { groupsBuilder.build(it) }
        else -> RoutingAction.noop()
    }
}
