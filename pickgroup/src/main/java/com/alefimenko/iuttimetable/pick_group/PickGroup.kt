package com.alefimenko.iuttimetable.pick_group

import com.alefimenko.iuttimetable.common.CanProvideContext
import com.alefimenko.iuttimetable.data.GroupInfo
import com.alefimenko.iuttimetable.data.Institute
import com.badoo.ribs.core.Rib
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PickGroup : Rib {

    interface Dependency : CanProvideContext {
        fun pickGroupInput(): ObservableSource<Input>
        fun pickGroupOutput(): Consumer<Output>
    }

    sealed class Input {
        data class GroupInfoReceived(val form: Int, val institute: Institute) : Input()
    }

    sealed class Output {
        object GoBack : Output()
        data class GoToSchedule(val groupInfo: GroupInfo) : Output()
    }
}
