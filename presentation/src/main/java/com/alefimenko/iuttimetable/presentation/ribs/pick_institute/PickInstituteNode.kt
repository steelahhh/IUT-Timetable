package com.alefimenko.iuttimetable.presentation.ribs.pick_institute

import android.os.Bundle
import android.view.ViewGroup
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.feature.PickInstituteFeature
import com.badoo.ribs.core.Node
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class PickInstituteNode internal constructor(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> PickInstituteView?)?,
    private val router: PickInstituteRouter,
    private val input: ObservableSource<PickInstitute.Input>,
    private val output: Consumer<PickInstitute.Output>,
    private val feature: PickInstituteFeature,
    private val interactor: PickInstituteInteractor
) : Node<PickInstituteView>(
    savedInstanceState = savedInstanceState,
    identifier = object : PickInstitute {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
)
