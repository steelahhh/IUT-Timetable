@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.presentation.ribs.pick_institute.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstitute.Input
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstitute.Output
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstituteInteractor
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstituteNode
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstituteRouter
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstituteViewImpl
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.feature.PickInstituteFeature
import com.badoo.mvicore.android.AndroidTimeCapsule
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import javax.inject.Named

@dagger.Module(includes = [DataModule::class])
internal object PickInstituteModule {

    @PickInstituteScope
    @Provides
    @Named(PickInstituteFeature.CAPSULE_KEY)
    @JvmStatic
    internal fun timeCapsule(savedInstanceState: Bundle?) = AndroidTimeCapsule(savedInstanceState)

    @PickInstituteScope
    @Provides
    @JvmStatic
    internal fun router(
        component: PickInstituteComponent,
        savedInstanceState: Bundle?
    ): PickInstituteRouter = PickInstituteRouter(
        savedInstanceState = savedInstanceState,
        transitionHandler = null
    )

    @PickInstituteScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: PickInstituteRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        @Named(PickInstituteFeature.CAPSULE_KEY)
        timeCapsule: AndroidTimeCapsule,
        feature: PickInstituteFeature
    ): PickInstituteInteractor = PickInstituteInteractor(
        savedInstanceState = savedInstanceState,
        router = router,
        input = input,
        output = output,
        feature = feature,
        timeCapsule = timeCapsule
    )

    @PickInstituteScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        router: PickInstituteRouter,
        interactor: PickInstituteInteractor,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: PickInstituteFeature
    ): PickInstituteNode = PickInstituteNode(
        savedInstanceState = savedInstanceState,
        viewFactory = PickInstituteViewImpl.Factory().invoke(true),
        router = router,
        interactor = interactor,
        input = input,
        output = output,
        feature = feature
    )
}
