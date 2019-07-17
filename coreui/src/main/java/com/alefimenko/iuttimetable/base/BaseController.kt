package com.alefimenko.iuttimetable.base

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alefimenko.iuttimetable.createBinder
import com.bluelinelabs.conductor.archlifecycle.LifecycleController

/*
 * Created by Alexander Efimenko on 2019-04-24.
 */

abstract class BaseController : LifecycleController(), ComponentCallbacks {

    open var layoutRes: Int = 0

    private val mainHandler = Handler(Looper.getMainLooper())

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

    override fun onAttach(view: View) {
        (activity as BaseActivity).run {
            window.setBackgroundDrawable(null)
        }
        super.onAttach(view)
    }

    override fun onDestroyView(view: View) {
        bind.resetViews()
        mainHandler.removeCallbacksAndMessages(null)
        super.onDestroyView(view)
    }

    fun post(action: () -> Unit) {
        mainHandler.post(action)
    }

    override fun onLowMemory() = Unit

    override fun onConfigurationChanged(newConfig: Configuration?) = Unit
}
