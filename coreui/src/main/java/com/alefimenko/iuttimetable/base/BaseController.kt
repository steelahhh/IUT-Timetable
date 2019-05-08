package com.alefimenko.iuttimetable.base

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alefimenko.iuttimetable.common.IUTRefWatcher
import com.alefimenko.iuttimetable.createBinder
import com.alefimenko.iuttimetable.extension.requireActivity
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

/*
 * Created by Alexander Efimenko on 2019-04-24.
 */

abstract class BaseController<Event, ViewModel>(
    private val events: PublishSubject<Event> = PublishSubject.create()
) : LifecycleController(),
    Consumer<ViewModel>,
    ObservableSource<Event> by events,
    ComponentCallbacks {

    private var hasExited: Boolean = false

    open var layoutRes: Int = 0

    val mainHandler = Handler(Looper.getMainLooper())

    val bind = createBinder()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(layoutRes, container, false).also {
            it.post {
                onViewBound(it)
            }
        }
    }

    open fun onViewBound(view: View) {
    }

    fun dispatch(event: Event) {
        events.onNext(event)
    }

    final override fun accept(viewmodel: ViewModel) {
        if (isAttached) {
            acceptViewModel(viewmodel)
        }
    }

    abstract fun acceptViewModel(viewModel: ViewModel)

    override fun onAttach(view: View) {
        (activity as BaseActivity).run {
            updateNavigationColor()
            window.setBackgroundDrawable(null)
        }
        super.onAttach(view)
    }

    override fun onDestroyView(view: View) {
        bind.resetViews()
        mainHandler.removeCallbacksAndMessages(null)
        super.onDestroyView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (hasExited) (requireActivity().application as IUTRefWatcher).refWatcher?.watch(this)
    }

    override fun onChangeEnded(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        super.onChangeEnded(changeHandler, changeType)
        hasExited = !changeType.isEnter
        if (isDestroyed) (requireActivity().application as IUTRefWatcher).refWatcher?.watch(this)
    }

    override fun onLowMemory() = Unit

    override fun onConfigurationChanged(newConfig: Configuration?) = Unit
}