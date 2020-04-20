package com.alefimenko.iuttimetable.pick_institute

import android.view.ViewGroup
import com.alefimenko.iuttimetable.pick_institute.feature.PickInstituteFeature
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class PickInstituteNode internal constructor(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ((ViewGroup) -> PickInstituteView?)?,
    private val router: PickInstituteRouter,
    private val input: ObservableSource<PickInstitute.Input>,
    private val output: Consumer<PickInstitute.Output>,
    private val feature: PickInstituteFeature,
    private val interactor: PickInstituteInteractor
) : Node<PickInstituteView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
)
