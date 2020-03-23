package com.alefimenko.iuttimetable.presentation.ribs.pick_group

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupView.Event
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroupView.ViewModel
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PickGroupView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, PickGroupView>
}

class PickGroupViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : PickGroupView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_pick_group
    ) : PickGroupView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> PickGroupView = {
            PickGroupViewImpl(inflate(it, layoutRes))
        }
    }

    override fun accept(vm: PickGroupView.ViewModel) = Unit
}
