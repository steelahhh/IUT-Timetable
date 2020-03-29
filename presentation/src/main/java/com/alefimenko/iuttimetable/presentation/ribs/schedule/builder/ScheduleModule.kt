@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.presentation.ribs.schedule.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.data.date.DateInteractor
import com.alefimenko.iuttimetable.data.date.DateInteractorImpl
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.presentation.ribs.schedule.Schedule
import com.alefimenko.iuttimetable.presentation.ribs.schedule.Schedule.Input
import com.alefimenko.iuttimetable.presentation.ribs.schedule.Schedule.Output
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleInteractor
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleNode
import com.alefimenko.iuttimetable.presentation.ribs.schedule.ScheduleRouter
import com.alefimenko.iuttimetable.presentation.ribs.schedule.feature.ScheduleFeature
import com.badoo.mvicore.android.AndroidTimeCapsule
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import javax.inject.Named

@dagger.Module(includes = [DataModule::class])
internal object ScheduleModule {

    @ScheduleScope
    @Provides
    @Named(ScheduleFeature.CAPSULE_KEY)
    @JvmStatic
    internal fun timeCapsule(savedInstanceState: Bundle?) = AndroidTimeCapsule(savedInstanceState)

    @ScheduleScope
    @Provides
    @JvmStatic
    fun provideDateInteractor(preferences: Preferences): DateInteractor = DateInteractorImpl(preferences)

    @ScheduleScope
    @Provides
    @JvmStatic
    internal fun router(
        component: ScheduleComponent,
        savedInstanceState: Bundle?,
        customisation: Schedule.Customisation
    ): ScheduleRouter = ScheduleRouter(
        savedInstanceState = savedInstanceState,
        transitionHandler = null // Add customisation.transitionHandler if you need it
    )

    @ScheduleScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: ScheduleRouter,
        @Named(ScheduleFeature.CAPSULE_KEY)
        timeCapsule: AndroidTimeCapsule,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: ScheduleFeature
    ): ScheduleInteractor = ScheduleInteractor(
        savedInstanceState = savedInstanceState,
        router = router,
        input = input,
        output = output,
        timeCapsule = timeCapsule,
        feature = feature
    )

    @ScheduleScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: Schedule.Customisation,
        router: ScheduleRouter,
        interactor: ScheduleInteractor,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: ScheduleFeature
    ): ScheduleNode = ScheduleNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor,
        input = input,
        output = output,
        feature = feature
    )
}
