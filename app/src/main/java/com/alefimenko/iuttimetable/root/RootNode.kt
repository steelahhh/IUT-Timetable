package com.alefimenko.iuttimetable.root

import android.os.Bundle
import com.alefimenko.iuttimetable.root.feature.RootFeature
import com.badoo.ribs.core.Node

class RootNode internal constructor(
    savedInstanceState: Bundle?,
    private val router: RootRouter,
    private val feature: RootFeature,
    private val interactor: RootInteractor
) : Node<Nothing>(
    savedInstanceState = savedInstanceState,
    identifier = object : Root {},
    viewFactory = null,
    router = router,
    interactor = interactor
)
