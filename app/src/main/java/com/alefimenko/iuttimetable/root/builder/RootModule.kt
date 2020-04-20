@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.root.builder

import com.alefimenko.iuttimetable.AppRibCustomisations
import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.pick_group_root.PickGroupRoot
import com.alefimenko.iuttimetable.pick_group_root.builder.PickGroupRootBuilder
import com.alefimenko.iuttimetable.root.RootInteractor
import com.alefimenko.iuttimetable.root.RootNode
import com.alefimenko.iuttimetable.root.RootRouter
import com.alefimenko.iuttimetable.root.feature.RootFeature
import com.alefimenko.iuttimetable.schedule.Schedule
import com.alefimenko.iuttimetable.schedule.builder.ScheduleBuilder
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.customisation.RibCustomisationDirectory
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module(includes = [DataModule::class])
internal object RootModule {

    @RootScope
    @Provides
    @JvmStatic
    internal fun router(
        component: RootComponent,
        buildParams: BuildParams<Nothing?>
    ): RootRouter = RootRouter(
        buildParams = buildParams,
        pickGroupRootBuilder = PickGroupRootBuilder(component),
        scheduleBuilder = ScheduleBuilder(component)
    )

    @RootScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: RootRouter,
        feature: RootFeature
    ): RootInteractor = RootInteractor(
        buildParams = buildParams,
        router = router,
        feature = feature
    )

    @RootScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        router: RootRouter,
        interactor: RootInteractor,
        feature: RootFeature
    ): RootNode = RootNode(
        buildParams = buildParams,
        router = router,
        interactor = interactor,
        feature = feature
    )

    @RootScope
    @Provides
    @JvmStatic
    internal fun rootGroupInput(): ObservableSource<PickGroupRoot.Input> = Observable.empty()

    @RootScope
    @Provides
    @JvmStatic
    internal fun rootGroupOutput(
        rootInteractor: RootInteractor
    ): Consumer<PickGroupRoot.Output> = rootInteractor.pickGroupNavigator

    @RootScope
    @Provides
    @JvmStatic
    internal fun scheduleInput(
        rootInteractor: RootInteractor
    ): ObservableSource<Schedule.Input> = rootInteractor.scheduleInput

    @RootScope
    @Provides
    @JvmStatic
    internal fun scheduleOutput(
        rootInteractor: RootInteractor
    ): Consumer<Schedule.Output> = rootInteractor.scheduleOutput

    @Provides
    @JvmStatic
    internal fun customisation(): RibCustomisationDirectory = AppRibCustomisations
}
