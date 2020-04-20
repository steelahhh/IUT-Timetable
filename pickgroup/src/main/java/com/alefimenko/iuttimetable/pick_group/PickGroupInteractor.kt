package com.alefimenko.iuttimetable.pick_group

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.alefimenko.iuttimetable.pick_group.feature.PickGroupFeature
import com.alefimenko.iuttimetable.pick_group.mapper.InputToWish
import com.alefimenko.iuttimetable.pick_group.mapper.NewsToOutput
import com.alefimenko.iuttimetable.pick_group.mapper.StateToViewModel
import com.alefimenko.iuttimetable.pick_group.mapper.ViewEventToWish
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.builder.BuildParams
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

internal class PickGroupInteractor(
    buildParams: BuildParams<Nothing?>,
    private val router: PickGroupRouter,
    private val timeCapsule: AndroidTimeCapsule,
    private val input: ObservableSource<PickGroup.Input>,
    private val output: Consumer<PickGroup.Output>,
    private val feature: PickGroupFeature
) : Interactor<PickGroupView>(
    buildParams = buildParams,
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
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timeCapsule.saveState(outState)
    }
}
