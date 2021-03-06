package com.alefimenko.iuttimetable.pick_group_root

import com.alefimenko.iuttimetable.common.CanProvideContext
import com.alefimenko.iuttimetable.data.GroupInfo
import com.badoo.ribs.core.Rib
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PickGroupRoot : Rib {

    interface Dependency : CanProvideContext {
        fun pickGroupRootInput(): ObservableSource<Input>
        fun pickGroupRootOutput(): Consumer<Output>
    }

    sealed class Input

    sealed class Output {
        object GoBack : Output()
        data class OpenSchedule(val groupInfo: GroupInfo) : Output()
    }
}
