@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.presentation.ribs.groups.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.presentation.ribs.groups.Groups
import com.alefimenko.iuttimetable.presentation.ribs.groups.Groups.Input
import com.alefimenko.iuttimetable.presentation.ribs.groups.Groups.Output
import com.alefimenko.iuttimetable.presentation.ribs.groups.GroupsInteractor
import com.alefimenko.iuttimetable.presentation.ribs.groups.GroupsNode
import com.alefimenko.iuttimetable.presentation.ribs.groups.GroupsRouter
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module(includes = [DataModule::class])
internal object GroupsModule {

    @GroupsScope
    @Provides
    @JvmStatic
    internal fun router(
        component: GroupsComponent,
        savedInstanceState: Bundle?,
        customisation: Groups.Customisation
    ): GroupsRouter = GroupsRouter(
        savedInstanceState = savedInstanceState,
        transitionHandler = null
    )

    @GroupsScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: GroupsRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: GroupsFeature
    ): GroupsInteractor = GroupsInteractor(
        savedInstanceState = savedInstanceState,
        router = router,
        input = input,
        output = output,
        feature = feature
    )

    @GroupsScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: Groups.Customisation,
        router: GroupsRouter,
        interactor: GroupsInteractor,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: GroupsFeature
    ): GroupsNode = GroupsNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor,
        input = input,
        output = output,
        feature = feature
    )
}
