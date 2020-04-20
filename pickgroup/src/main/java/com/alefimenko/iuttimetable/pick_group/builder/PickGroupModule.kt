@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.pick_group.builder

import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.pick_group.PickGroup.Input
import com.alefimenko.iuttimetable.pick_group.PickGroup.Output
import com.alefimenko.iuttimetable.pick_group.PickGroupInteractor
import com.alefimenko.iuttimetable.pick_group.PickGroupNode
import com.alefimenko.iuttimetable.pick_group.PickGroupRouter
import com.alefimenko.iuttimetable.pick_group.PickGroupViewImpl
import com.alefimenko.iuttimetable.pick_group.feature.PickGroupFeature
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.core.builder.BuildParams
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
    internal fun timeCapsule(
        buildParams: BuildParams<Nothing?>
    ) = AndroidTimeCapsule(buildParams.savedInstanceState)

    @PickGroupScope
    @Provides
    @JvmStatic
    internal fun router(
        component: PickGroupComponent,
        buildParams: BuildParams<Nothing?>
    ): PickGroupRouter = PickGroupRouter(
        buildParams = buildParams,
        transitionHandler = null
    )

    @PickGroupScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: PickGroupRouter,
        @Named(PickGroupFeature.CAPSULE_KEY)
        timeCapsule: AndroidTimeCapsule,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: PickGroupFeature
    ): PickGroupInteractor = PickGroupInteractor(
        buildParams = buildParams,
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
        buildParams: BuildParams<Nothing?>,
        router: PickGroupRouter,
        interactor: PickGroupInteractor,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: PickGroupFeature
    ): PickGroupNode = PickGroupNode(
        buildParams = buildParams,
        viewFactory = PickGroupViewImpl.Factory().invoke(null),
        router = router,
        interactor = interactor,
        input = input,
        output = output,
        feature = feature
    )
}
