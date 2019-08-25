package com.alefimenko.iuttimetable.base

import android.os.Handler
import android.os.Looper
import com.airbnb.mvrx.BaseMvRxFragment

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

abstract class BaseFragment : BaseMvRxFragment() {
    private val handler = Handler(Looper.getMainLooper())

    open fun onBackPressed() = Unit

    fun post(action: () -> Unit) {
        handler.post(action)
    }
}
