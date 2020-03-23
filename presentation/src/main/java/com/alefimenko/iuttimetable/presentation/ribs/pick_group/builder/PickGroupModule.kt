@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.presentation.ribs.pick_group.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup.Input
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup.Output
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupInteractor
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupNode
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupRouter
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupViewImpl
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object PickGroupModule {

    @PickGroupScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: PickGroupComponent,
        savedInstanceState: Bundle?
    ): PickGroupRouter =
        PickGroupRouter(
            savedInstanceState = savedInstanceState,
            transitionHandler = null // Add customisation.transitionHandler if you need it
        )

    @PickGroupScope
    @Provides
    @JvmStatic
    internal fun feature(): PickGroupFeature =
        PickGroupFeature()

    @PickGroupScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: PickGroupRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: PickGroupFeature
    ): PickGroupInteractor =
        PickGroupInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            input = input,
            output = output,
            feature = feature
        )

    @PickGroupScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        router: PickGroupRouter,
        interactor: PickGroupInteractor,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: PickGroupFeature
    ): PickGroupNode = PickGroupNode(
        savedInstanceState = savedInstanceState,
        viewFactory = PickGroupViewImpl.Factory().invoke(null),
        router = router,
        interactor = interactor,
        input = input,
        output = output,
        feature = feature
    )
}
