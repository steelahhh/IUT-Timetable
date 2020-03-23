package com.alefimenko.iuttimetable.root

import android.os.Bundle
import android.os.Parcelable
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.builder.PickGroupRootBuilder
import com.alefimenko.iuttimetable.root.RootRouter.Configuration
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class RootRouter(
    savedInstanceState: Bundle?,
    transitionHandler: TransitionHandler<Configuration>? = null,
    private val pickGroupRootBuilder: PickGroupRootBuilder
) : Router<Configuration, Nothing, Configuration, Nothing, Nothing>(
    savedInstanceState = savedInstanceState,
    transitionHandler = transitionHandler,
    initialConfiguration = Configuration.Splash,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Splash : Configuration()
        @Parcelize object Schedule : Configuration()
        @Parcelize object PickGroup : Configuration()
    }

    override fun resolveConfiguration(
        configuration: Configuration
    ): RoutingAction<Nothing> = when (configuration) {
        is Configuration.PickGroup -> attach { pickGroupRootBuilder.build(it) }
        else -> RoutingAction.noop()
    }
}
