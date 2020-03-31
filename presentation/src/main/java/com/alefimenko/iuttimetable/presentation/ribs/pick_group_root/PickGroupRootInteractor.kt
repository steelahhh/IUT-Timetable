package com.alefimenko.iuttimetable.presentation.ribs.pick_group_root

import android.os.Bundle
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRootRouter.Configuration.Content
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.PickInstitute
import com.badoo.ribs.core.Interactor
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

internal class PickGroupRootInteractor(
    savedInstanceState: Bundle?,
    private val router: PickGroupRootRouter,
    private val input: ObservableSource<PickGroupRoot.Input>,
    private val output: Consumer<PickGroupRoot.Output>
) : Interactor<Nothing>(
    savedInstanceState = savedInstanceState,
    disposables = null
) {
    val pickGroupInput = PublishSubject.create<PickGroup.Input>()

    val pickInstituteOutputConsumer: Consumer<PickInstitute.Output> = Consumer {
        when (it) {
            is PickInstitute.Output.RouteToPickInstitute -> {
                router.push(Content.PickGroup)
                pickGroupInput.onNext(PickGroup.Input.GroupInfoReceived(it.form, it.institute))
            }
            is PickInstitute.Output.GoBack -> output.accept(PickGroupRoot.Output.GoBack)
        }
    }

    val pickGroupOutputConsumer: Consumer<PickGroup.Output> = Consumer {
        when (it) {
            is PickGroup.Output.GoBack -> router.popBackStack()
            is PickGroup.Output.GoToSchedule -> output.accept(PickGroupRoot.Output.OpenSchedule(it.groupInfo))
        }
    }
}
