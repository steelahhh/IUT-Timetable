package com.alefimenko.iuttimetable.schedule

import android.os.Bundle
import android.view.ViewGroup
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature
import com.badoo.ribs.core.Node
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class ScheduleNode internal constructor(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> ScheduleView?)?,
    private val router: ScheduleRouter,
    private val input: ObservableSource<Schedule.Input>,
    private val output: Consumer<Schedule.Output>,
    private val feature: ScheduleFeature,
    private val interactor: ScheduleInteractor
) : Node<ScheduleView>(
    savedInstanceState = savedInstanceState,
    identifier = object : Schedule {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
)
