package com.alefimenko.iuttimetable.presentation.ribs.pick_group_root

import android.os.Bundle
import com.badoo.ribs.core.Interactor
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

internal class PickGroupRootInteractor(
    savedInstanceState: Bundle?,
    private val router: PickGroupRootRouter,
    private val input: ObservableSource<PickGroupRoot.Input>,
    private val output: Consumer<PickGroupRoot.Output>
) : Interactor<Nothing>(
    savedInstanceState = savedInstanceState,
    disposables = null
)
