package com.alefimenko.iuttimetable.groups

import android.os.Parcelable
import com.alefimenko.iuttimetable.groups.GroupsRouter.Configuration
import com.alefimenko.iuttimetable.groups.GroupsRouter.Configuration.Content
import com.alefimenko.iuttimetable.groups.GroupsRouter.Configuration.Overlay
import com.alefimenko.iuttimetable.groups.GroupsRouter.Configuration.Permanent
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class GroupsRouter(
    buildParams: BuildParams<Nothing?>,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration, Permanent, Content, Overlay, GroupsView>(
    buildParams = buildParams,
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

    override fun resolveConfiguration(configuration: Configuration): RoutingAction = RoutingAction.noop()
}
