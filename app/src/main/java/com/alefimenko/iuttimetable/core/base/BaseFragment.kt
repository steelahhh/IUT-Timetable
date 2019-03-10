package com.alefimenko.iuttimetable.core.base

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alefimenko.iuttimetable.util.createBinder
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import org.koin.standalone.KoinComponent

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

abstract class BaseController<Event, ViewModel>(
    private val events: PublishSubject<Event> = PublishSubject.create()
) : LifecycleController(), Consumer<ViewModel>, ObservableSource<Event> by events, ComponentCallbacks {

    val bind = createBinder()

    override fun onLowMemory() = Unit

    override fun onConfigurationChanged(newConfig: Configuration?) = Unit

    fun dispatch(event: Event) {
        events.onNext(event)
    }

    override fun onAttach(view: View) {
        (activity as BaseActivity).updateNavigationColor()
        super.onAttach(view)
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        bind.resetViews()
    }
}
