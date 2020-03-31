package com.alefimenko.iuttimetable.presentation.ribs.pick_institute

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.feature.PickInstituteFeature
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.mapper.InputToWish
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.mapper.NewsToOutput
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.mapper.StateToViewModel
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.mapper.ViewEventToWish
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

internal class PickInstituteInteractor(
    savedInstanceState: Bundle?,
    private val router: PickInstituteRouter,
    private val timeCapsule: AndroidTimeCapsule,
    private val input: ObservableSource<PickInstitute.Input>,
    private val output: Consumer<PickInstitute.Output>,
    private val feature: PickInstituteFeature
) : Interactor<PickInstituteView>(
    savedInstanceState = savedInstanceState,
    disposables = feature
) {

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        ribLifecycle.createDestroy {
            bind(feature.news to output using NewsToOutput)
            bind(input to feature using InputToWish)
        }
    }

    override fun onViewCreated(view: PickInstituteView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to output using ViewEventToOutput)
            bind(view to feature using ViewEventToWish)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timeCapsule.saveState(outState)
    }

    private object ViewEventToOutput : (PickInstituteView.Event) -> PickInstitute.Output? {
        override fun invoke(event: PickInstituteView.Event): PickInstitute.Output? = when (event) {
            PickInstituteView.Event.GoBack -> PickInstitute.Output.GoBack
            else -> null
        }
    }
}
