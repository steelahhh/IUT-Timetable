package com.alefimenko.iuttimetable.pick_group_root

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class PickGroupRootNode internal constructor(
    buildParams: BuildParams<Boolean>,
    private val router: PickGroupRootRouter,
    private val input: ObservableSource<PickGroupRoot.Input>,
    private val output: Consumer<PickGroupRoot.Output>,
    private val interactor: PickGroupRootInteractor
) : Node<Nothing>(
    buildParams = buildParams,
    viewFactory = null,
    router = router,
    interactor = interactor
)
