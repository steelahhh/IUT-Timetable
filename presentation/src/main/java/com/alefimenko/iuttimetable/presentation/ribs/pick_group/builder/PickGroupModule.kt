@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.presentation.ribs.pick_group.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup.Input
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup.Output
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupInteractor
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupNode
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupRouter
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupViewImpl
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature
import com.badoo.mvicore.android.AndroidTimeCapsule
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import javax.inject.Named

@dagger.Module(includes = [DataModule::class])
internal object PickGroupModule {

    @PickGroupScope
    @Provides
    @Named(PickGroupFeature.CAPSULE_KEY)
    @JvmStatic
    internal fun timeCapsule(savedInstanceState: Bundle?) = AndroidTimeCapsule(savedInstanceState)

    @PickGroupScope
    @Provides
    @JvmStatic
    internal fun router(
        component: PickGroupComponent,
        savedInstanceState: Bundle?
    ): PickGroupRouter = PickGroupRouter(
        savedInstanceState = savedInstanceState,
        transitionHandler = null
    )

    @PickGroupScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: PickGroupRouter,
        @Named(PickGroupFeature.CAPSULE_KEY)
        timeCapsule: AndroidTimeCapsule,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: PickGroupFeature
    ): PickGroupInteractor = PickGroupInteractor(
        savedInstanceState = savedInstanceState,
        router = router,
        timeCapsule = timeCapsule,
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
