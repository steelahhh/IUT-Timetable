package com.alefimenko.iuttimetable.pick_group

import android.view.ViewGroup
import com.alefimenko.iuttimetable.pick_group.feature.PickGroupFeature
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class PickGroupNode internal constructor(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ((ViewGroup) -> PickGroupView?)?,
    private val router: PickGroupRouter,
    private val input: ObservableSource<PickGroup.Input>,
    private val output: Consumer<PickGroup.Output>,
    private val feature: PickGroupFeature,
    private val interactor: PickGroupInteractor
) : Node<PickGroupView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
)
