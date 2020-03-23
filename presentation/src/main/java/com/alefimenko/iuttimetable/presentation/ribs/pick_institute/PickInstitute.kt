package com.alefimenko.iuttimetable.presentation.ribs.pick_institute

import com.alefimenko.iuttimetable.common.ContextProvider
import com.badoo.ribs.core.Rib
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import javax.inject.Named

interface PickInstitute : Rib {

    interface Dependency : ContextProvider {
        fun pickInstituteInput(): ObservableSource<Input>
        fun pickInstituteOutput(): Consumer<Output>
        @Named("Thingy")
        fun isRoot(): Boolean
    }

    sealed class Input

    sealed class Output
}
