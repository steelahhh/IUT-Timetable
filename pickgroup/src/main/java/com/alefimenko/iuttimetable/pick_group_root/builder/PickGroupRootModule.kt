@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.pick_group_root.builder

import com.alefimenko.iuttimetable.pick_group.PickGroup
import com.alefimenko.iuttimetable.pick_group.builder.PickGroupBuilder
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRoot.Input
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRoot.Output
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRootInteractor
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRootNode
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRootRouter
import com.alefimenko.iuttimetable.pick_institute.PickInstitute
import com.alefimenko.iuttimetable.pick_institute.builder.PickInstituteBuilder
import com.badoo.ribs.core.builder.BuildParams
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
        buildParams: BuildParams<Boolean>
    ): PickGroupRootRouter = PickGroupRootRouter(
        buildParams = buildParams,
        transitionHandler = Slider(),
        pickGroupBuilder = PickGroupBuilder(component),
        pickInstituteBuilder = PickInstituteBuilder(component)
    )

    @PickGroupRootScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Boolean>,
        router: PickGroupRootRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>
    ): PickGroupRootInteractor = PickGroupRootInteractor(
        buildParams = buildParams,
        router = router,
        input = input,
        output = output
    )

    @PickGroupRootScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Boolean>,
        router: PickGroupRootRouter,
        interactor: PickGroupRootInteractor,
        input: ObservableSource<Input>,
        output: Consumer<Output>
    ): PickGroupRootNode = PickGroupRootNode(
        buildParams = buildParams,
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
