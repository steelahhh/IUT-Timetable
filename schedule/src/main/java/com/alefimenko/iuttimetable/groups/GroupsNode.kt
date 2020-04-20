package com.alefimenko.iuttimetable.groups

import android.view.ViewGroup
import com.alefimenko.iuttimetable.groups.feature.GroupsFeature
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class GroupsNode internal constructor(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ((ViewGroup) -> GroupsView?)?,
    private val router: GroupsRouter,
    private val input: ObservableSource<Groups.Input>,
    private val output: Consumer<Groups.Output>,
    private val feature: GroupsFeature,
    private val interactor: GroupsInteractor
) : Node<GroupsView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
)
