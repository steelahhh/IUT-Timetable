package com.alefimenko.iuttimetable.pick_group_root

import android.os.Parcelable
import com.alefimenko.iuttimetable.pick_group.builder.PickGroupBuilder
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRootRouter.Configuration
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRootRouter.Configuration.Content
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRootRouter.Configuration.Overlay
import com.alefimenko.iuttimetable.pick_institute.builder.PickInstituteBuilder
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class PickGroupRootRouter(
    private val pickGroupBuilder: PickGroupBuilder,
    private val pickInstituteBuilder: PickInstituteBuilder,
    buildParams: BuildParams<Boolean>,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration, Nothing, Content, Overlay, Nothing>(
    buildParams = buildParams,
    transitionHandler = transitionHandler,
    initialConfiguration = Content.PickInstitute,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object PickInstitute : Content()
            @Parcelize object PickGroup : Content()
        }

        sealed class Overlay : Configuration() {
            @Parcelize object InstitutePicker : Overlay()
        }
    }

    override fun resolveConfiguration(
        configuration: Configuration
    ): RoutingAction<Nothing> = when (configuration) {
        is Content.PickGroup -> attach { pickGroupBuilder.build(it) }
        is Content.PickInstitute -> attach { pickInstituteBuilder.build(it) }
        else -> RoutingAction.noop()
    }
}
