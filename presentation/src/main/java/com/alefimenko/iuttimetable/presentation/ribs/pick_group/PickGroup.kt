package com.alefimenko.iuttimetable.presentation.ribs.pick_group

import com.badoo.ribs.core.Rib
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PickGroup : Rib {

    interface Dependency {
        fun pickGroupInput(): ObservableSource<Input>
        fun pickGroupOutput(): Consumer<Output>
    }

    sealed class Input

    sealed class Output
}
