@file:SuppressWarnings("LongParameterList", "LongMethod")

package com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.builder

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.builder.PickGroupBuilder
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRoot.Input
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRoot.Output
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRootInteractor
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRootNode
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRootRouter
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstitute
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.builder.PickInstituteBuilder
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import javax.inject.Named

@dagger.Module
internal object PickGroupRootModule {

    @PickGroupRootScope
    @Provides
    @JvmStatic
    internal fun router(
        component: PickGroupRootComponent,
        savedInstanceState: Bundle?
    ): PickGroupRootRouter = PickGroupRootRouter(
        savedInstanceState = savedInstanceState,
        transitionHandler = null,
        pickGroupBuilder = PickGroupBuilder(object : PickGroup.Dependency {
            override fun pickGroupInput(): ObservableSource<PickGroup.Input> = Observable.empty()

            override fun pickGroupOutput(): Consumer<PickGroup.Output> = Consumer { }
        }),
        pickInstituteBuilder = PickInstituteBuilder(component)
    )

    @PickGroupRootScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: PickGroupRootRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>
    ): PickGroupRootInteractor = PickGroupRootInteractor(
        savedInstanceState = savedInstanceState,
        router = router,
        input = input,
        output = output
    )

    @PickGroupRootScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        router: PickGroupRootRouter,
        interactor: PickGroupRootInteractor,
        input: ObservableSource<Input>,
        output: Consumer<Output>
    ): PickGroupRootNode = PickGroupRootNode(
        savedInstanceState = savedInstanceState,
        router = router,
        interactor = interactor,
        input = input,
        output = output
    )

    @Provides
    @JvmStatic
    internal fun pickInstituteOutput(): ObservableSource<PickInstitute.Input> = Observable.empty()

    @Provides
    @JvmStatic
    internal fun pickInstIn(): Consumer<PickInstitute.Output> = Consumer { }

    @Provides
    @JvmStatic
    @Named("Thingy")
    fun isRootScren() = true
}
