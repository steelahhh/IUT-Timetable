package com.alefimenko.iuttimetable.groups

import android.os.Bundle
import android.view.ViewGroup
import com.alefimenko.iuttimetable.groups.feature.GroupsFeature
import com.badoo.ribs.core.Node
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class GroupsNode internal constructor(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> GroupsView?)?,
    private val router: GroupsRouter,
    private val input: ObservableSource<Groups.Input>,
    private val output: Consumer<Groups.Output>,
    private val feature: GroupsFeature,
    private val interactor: GroupsInteractor
) : Node<GroupsView>(
    savedInstanceState = savedInstanceState,
    identifier = object : Groups {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
)
