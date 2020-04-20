package com.alefimenko.iuttimetable.root

import com.alefimenko.iuttimetable.root.feature.RootFeature
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams

class RootNode internal constructor(
    buildParams: BuildParams<Nothing?>,
    private val router: RootRouter,
    private val feature: RootFeature,
    private val interactor: RootInteractor
) : Node<Nothing>(
    buildParams = buildParams,
    viewFactory = null,
    router = router,
    interactor = interactor
)
