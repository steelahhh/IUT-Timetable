package com.alefimenko.iuttimetable.base

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.*

/*
 * Created by Alexander Efimenko on 2019-04-21.
 */

abstract class KotlinView(
    layoutRes: Int,
    inflater: LayoutInflater,
    container: ViewGroup
) : LayoutContainer {

    private val mainHandler = Handler(Looper.getMainLooper())

    private var _containerView: View? = inflater.inflate(layoutRes, container, false)
    override val containerView: View get() = _containerView ?: error("Error inflating view: ${javaClass.simpleName}")

    protected open fun tearDown() {
        clearFindViewByIdCache()
        mainHandler.removeCallbacksAndMessages(null)
        _containerView = null
    }

    fun post(action: () -> Unit) {
        mainHandler.post(action)
    }
}
