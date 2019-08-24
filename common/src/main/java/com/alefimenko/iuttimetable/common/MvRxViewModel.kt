package com.alefimenko.iuttimetable.common

import android.os.Handler
import android.os.Looper
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

abstract class MvRxViewModel<S : MvRxState>(
    initialState: S
) : BaseMvRxViewModel<S>(initialState, debugMode = BuildConfig.DEBUG) {
    private val handler = Handler(Looper.getMainLooper())

    fun post(action: () -> Unit) {
        handler.post(action)
    }
}
