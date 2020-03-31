@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.builder.PickGroupBuilder
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRoot.Input
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRoot.Output
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRootInteractor
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRootNode
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRootRouter
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstitute
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.builder.PickInstituteBuilder
import com.badoo.ribs.core.routing.transition.handler.Slider
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object PickGroupRootModule {

    @PickGroupRootScope
    @Provides
    @JvmStatic
    internal fun router(
        component: PickGroupRootComponent,
        savedInstanceState: Bundle?
    ): PickGroupRootRouter = PickGroupRootRouter(
        savedInstanceState = savedInstanceState,
        transitionHandler = Slider(),
        pickGroupBuilder = PickGroupBuilder(component),
        pickInstituteBuilder = PickInstituteBuilder(component)
    )

    @PickGroupRootScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: PickGroupRootRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>
    ): PickGroupRootInteractor = PickGroupRootInteractor(
        savedInstanceState = savedInstanceState,
        router = router,
        input = input,
        output = output
    )

    @PickGroupRootScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        router: PickGroupRootRouter,
        interactor: PickGroupRootInteractor,
        input: ObservableSource<Input>,
        output: Consumer<Output>
    ): PickGroupRootNode = PickGroupRootNode(
        savedInstanceState = savedInstanceState,
        router = router,
        interactor = interactor,
        input = input,
        output = output
    )

    @PickGroupRootScope
    @Provides
    @JvmStatic
    internal fun pickInstituteInput(): ObservableSource<PickInstitute.Input> = Observable.empty()

    @PickGroupRootScope
    @Provides
    @JvmStatic
    fun pickInstituteOutput(
        pickGroupRootInteractor: PickGroupRootInteractor
    ): Consumer<PickInstitute.Output> = pickGroupRootInteractor.pickInstituteOutputConsumer

    @PickGroupRootScope
    @Provides
    @JvmStatic
    fun pickGroupInput(
        pickGroupRootInteractor: PickGroupRootInteractor
    ): ObservableSource<PickGroup.Input> = pickGroupRootInteractor.pickGroupInput

    @PickGroupRootScope
    @Provides
    @JvmStatic
    fun pickGroupOutput(
        pickGroupRootInteractor: PickGroupRootInteractor
    ): Consumer<PickGroup.Output> = pickGroupRootInteractor.pickGroupOutputConsumer
}
