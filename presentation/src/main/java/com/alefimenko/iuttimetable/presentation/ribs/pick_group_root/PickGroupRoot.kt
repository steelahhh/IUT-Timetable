package com.alefimenko.iuttimetable.presentation.ribs.pick_group_root

import com.alefimenko.iuttimetable.common.ContextProvider
import com.alefimenko.iuttimetable.data.GroupInfo
import com.badoo.ribs.core.Rib
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PickGroupRoot : Rib {

    interface Dependency : ContextProvider {
        fun pickGroupRootInput(): ObservableSource<Input>
        fun pickGroupRootOutput(): Consumer<Output>
    }

    sealed class Input

    sealed class Output {
        data class OpenSchedule(val groupInfo: GroupInfo) : Output()
    }
}
