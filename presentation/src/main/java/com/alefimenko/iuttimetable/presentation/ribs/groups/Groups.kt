package com.alefimenko.iuttimetable.presentation.ribs.groups

import com.alefimenko.iuttimetable.common.CanProvideContext
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface Groups : Rib {

    interface Dependency : CanProvideRibCustomisation, CanProvideContext {
        fun groupsInput(): ObservableSource<Input>
        fun groupsOutput(): Consumer<Output>
    }

    sealed class Input

    sealed class Output {
        object Dismiss : Output()
        object AddNewGroup : Output()
    }

    class Customisation(
        val viewFactory: GroupsView.Factory = GroupsViewImpl.Factory(),
        val transitionHandler: TransitionHandler<GroupsRouter.Configuration>? = null
    ) : RibCustomisation
}
