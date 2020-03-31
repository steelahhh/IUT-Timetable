package com.alefimenko.iuttimetable.presentation.ribs.groups

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature
import com.alefimenko.iuttimetable.presentation.ribs.groups.mapper.InputToWish
import com.alefimenko.iuttimetable.presentation.ribs.groups.mapper.NewsToOutput
import com.alefimenko.iuttimetable.presentation.ribs.groups.mapper.StateToViewModel
import com.alefimenko.iuttimetable.presentation.ribs.groups.mapper.ViewEventToWish
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import io.reactivex.Observable.wrap
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

internal class GroupsInteractor(
    savedInstanceState: Bundle?,
    private val router: GroupsRouter,
    private val input: ObservableSource<Groups.Input>,
    private val output: Consumer<Groups.Output>,
    private val feature: GroupsFeature
) : Interactor<GroupsView>(
    savedInstanceState = savedInstanceState,
    disposables = feature
) {

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        ribLifecycle.createDestroy {
            bind(feature.news to output using NewsToOutput)
            bind(input to feature using InputToWish)
        }
    }

    override fun onViewCreated(view: GroupsView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(wrap(feature).distinctUntilChanged() to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
        }
    }
}
