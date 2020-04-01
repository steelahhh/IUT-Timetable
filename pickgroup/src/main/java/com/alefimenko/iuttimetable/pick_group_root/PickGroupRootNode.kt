package com.alefimenko.iuttimetable.pick_group_root

import android.os.Bundle
import com.badoo.ribs.core.Node
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class PickGroupRootNode internal constructor(
    savedInstanceState: Bundle?,
    private val router: PickGroupRootRouter,
    private val input: ObservableSource<PickGroupRoot.Input>,
    private val output: Consumer<PickGroupRoot.Output>,
    private val interactor: PickGroupRootInteractor
) : Node<Nothing>(
    savedInstanceState = savedInstanceState,
    identifier = object :
        PickGroupRoot {},
    viewFactory = null,
    router = router,
    interactor = interactor
)
