package com.alefimenko.iuttimetable.presentation.ribs.schedule

import com.alefimenko.iuttimetable.common.CanProvideContext
import com.alefimenko.iuttimetable.data.GroupInfo
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface Schedule : Rib {

    interface Dependency : CanProvideRibCustomisation, CanProvideContext {
        fun scheduleInput(): ObservableSource<Input>
        fun scheduleOutput(): Consumer<Output>
    }

    sealed class Input {
        data class LoadSchedule(val groupInfo: GroupInfo) : Input()
    }

    sealed class Output

    class Customisation(
        val viewFactory: ScheduleView.Factory = ScheduleViewImpl.Factory(),
        val transitionHandler: TransitionHandler<ScheduleRouter.Configuration>? = null
    ) : RibCustomisation
}
