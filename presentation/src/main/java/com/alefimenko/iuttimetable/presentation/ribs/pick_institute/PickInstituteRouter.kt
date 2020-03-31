package com.alefimenko.iuttimetable.presentation.ribs.pick_institute

import android.os.Bundle
import android.os.Parcelable
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstituteRouter.Configuration
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstituteRouter.Configuration.Content
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstituteRouter.Configuration.Overlay
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstituteRouter.Configuration.Permanent
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import kotlinx.android.parcel.Parcelize

class PickInstituteRouter(
    savedInstanceState: Bundle?,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration, Permanent, Content, Overlay, PickInstituteView>(
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
    ): RoutingAction<PickInstituteView> = RoutingAction.noop()
}
