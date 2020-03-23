@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.root.builder

import android.content.Context
import android.os.Bundle
import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRoot
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.builder.PickGroupRootBuilder
import com.alefimenko.iuttimetable.root.RootInteractor
import com.alefimenko.iuttimetable.root.RootNode
import com.alefimenko.iuttimetable.root.RootRouter
import com.alefimenko.iuttimetable.root.feature.RootFeature
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module(includes = [DataModule::class])
internal object RootModule {

    @RootScope
    @Provides
    @JvmStatic
    internal fun router(
        component: RootComponent,
        savedInstanceState: Bundle?
    ): RootRouter = RootRouter(
        savedInstanceState = savedInstanceState,
        pickGroupRootBuilder = PickGroupRootBuilder(object : PickGroupRoot.Dependency {
            override fun pickGroupRootInput(): ObservableSource<PickGroupRoot.Input> = Observable.empty()
            override fun pickGroupRootOutput(): Consumer<PickGroupRoot.Output> = Consumer { }
            override val context: Context = component.context()
        })
    )

    @RootScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: RootRouter,
        feature: RootFeature
    ): RootInteractor = RootInteractor(
        savedInstanceState = savedInstanceState,
        router = router,
        feature = feature
    )

    @RootScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        router: RootRouter,
        interactor: RootInteractor,
        feature: RootFeature
    ): RootNode = RootNode(
        savedInstanceState = savedInstanceState,
        router = router,
        interactor = interactor,
        feature = feature
    )
}
