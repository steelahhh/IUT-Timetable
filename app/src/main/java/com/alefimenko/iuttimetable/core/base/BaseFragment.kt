package com.alefimenko.iuttimetable.core.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alefimenko.iuttimetable.util.createBinder
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

abstract class BaseFragment<Event, ViewModel>(
    private val events: PublishSubject<Event> = PublishSubject.create()
) : Fragment(), Consumer<ViewModel>, ObservableSource<Event> by events {

    val bind = createBinder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as BaseActivity).updateNavigationColor()
        super.onViewCreated(view, savedInstanceState)
    }

    fun dispatch(event: Event) {
        events.onNext(event)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bind.resetViews()
    }
}
