package com.alefimenko.iuttimetable.pick_group

import android.os.Bundle
import android.view.ViewGroup
import com.alefimenko.iuttimetable.pick_group.feature.PickGroupFeature
import com.badoo.ribs.core.Node
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class PickGroupNode internal constructor(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> PickGroupView?)?,
    private val router: PickGroupRouter,
    private val input: ObservableSource<PickGroup.Input>,
    private val output: Consumer<PickGroup.Output>,
    private val feature: PickGroupFeature,
    private val interactor: PickGroupInteractor
) : Node<PickGroupView>(
    savedInstanceState = savedInstanceState,
    identifier = object : PickGroup {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
)
