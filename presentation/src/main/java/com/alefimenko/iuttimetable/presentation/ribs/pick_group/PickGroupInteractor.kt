package com.alefimenko.iuttimetable.presentation.ribs.pick_group

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.analytics.PickGroupAnalytics
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.mapper.InputToWish
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.mapper.NewsToOutput
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.mapper.StateToViewModel
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.mapper.ViewEventToAnalyticsEvent
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.mapper.ViewEventToWish
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

internal class PickGroupInteractor(
    savedInstanceState: Bundle?,
    private val router: PickGroupRouter,
    private val input: ObservableSource<PickGroup.Input>,
    private val output: Consumer<PickGroup.Output>,
    private val feature: PickGroupFeature
) : Interactor<PickGroupView>(
    savedInstanceState = savedInstanceState,
    disposables = feature
) {

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        ribLifecycle.createDestroy {
            bind(feature.news to output using NewsToOutput)
            bind(input to feature using InputToWish)
        }
    }

    override fun onViewCreated(view: PickGroupView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
            bind(view to PickGroupAnalytics using ViewEventToAnalyticsEvent)
        }
    }
}
