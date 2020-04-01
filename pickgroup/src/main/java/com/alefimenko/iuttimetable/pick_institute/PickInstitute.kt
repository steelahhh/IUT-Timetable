package com.alefimenko.iuttimetable.pick_institute

import com.alefimenko.iuttimetable.common.CanProvideContext
import com.alefimenko.iuttimetable.data.Institute
import com.alefimenko.iuttimetable.data.local.Constants
import com.badoo.ribs.core.Rib
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import javax.inject.Named

interface PickInstitute : Rib {

    interface Dependency : CanProvideContext {
        fun pickInstituteInput(): ObservableSource<Input>
        fun pickInstituteOutput(): Consumer<Output>
        @Named(Constants.PICK_GROUP_ROOT)
        fun isRoot(): Boolean
    }

    sealed class Input

    sealed class Output {
        object GoBack : Output()
        data class RouteToPickInstitute(val form: Int, val institute: Institute) : Output()
    }
}
