package com.alefimenko.iuttimetable.root

import android.os.Parcelable
import com.alefimenko.iuttimetable.pick_group_root.builder.PickGroupRootBuilder
import com.alefimenko.iuttimetable.root.RootRouter.Configuration
import com.alefimenko.iuttimetable.schedule.builder.ScheduleBuilder
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class RootRouter(
    buildParams: BuildParams<Nothing?>,
    transitionHandler: TransitionHandler<Configuration>? = null,
    private val pickGroupRootBuilder: PickGroupRootBuilder,
    private val scheduleBuilder: ScheduleBuilder
) : Router<Configuration, Nothing, Configuration, Nothing, Nothing>(
    buildParams = buildParams,
    transitionHandler = transitionHandler,
    initialConfiguration = Configuration.Splash,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Splash : Configuration()
        @Parcelize object Schedule : Configuration()
        @Parcelize data class PickGroup(val isRoot: Boolean) : Configuration()
    }

    override fun resolveConfiguration(
        configuration: Configuration
    ): RoutingAction = when (configuration) {
        is Configuration.PickGroup -> attach { pickGroupRootBuilder.build(it, configuration.isRoot) }
        is Configuration.Schedule -> attach { scheduleBuilder.build(it) }
        else -> RoutingAction.noop()
    }
}
