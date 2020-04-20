@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.groups.builder

import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.groups.Groups
import com.alefimenko.iuttimetable.groups.Groups.Input
import com.alefimenko.iuttimetable.groups.Groups.Output
import com.alefimenko.iuttimetable.groups.GroupsInteractor
import com.alefimenko.iuttimetable.groups.GroupsNode
import com.alefimenko.iuttimetable.groups.GroupsRouter
import com.alefimenko.iuttimetable.groups.feature.GroupsFeature
import com.badoo.ribs.core.builder.BuildParams
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
        buildParams: BuildParams<Nothing?>,
        customisation: Groups.Customisation
    ): GroupsRouter = GroupsRouter(
        buildParams = buildParams,
        transitionHandler = null
    )

    @GroupsScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: GroupsRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: GroupsFeature
    ): GroupsInteractor = GroupsInteractor(
        buildParams = buildParams,
        router = router,
        input = input,
        output = output,
        feature = feature
    )

    @GroupsScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: Groups.Customisation,
        router: GroupsRouter,
        interactor: GroupsInteractor,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: GroupsFeature
    ): GroupsNode = GroupsNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor,
        input = input,
        output = output,
        feature = feature
    )
}
