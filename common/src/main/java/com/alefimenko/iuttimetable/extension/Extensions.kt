package com.alefimenko.iuttimetable.extension

import android.os.Build

/*
 * Created by Alexander Efimenko on 2019-04-27.
 */

inline fun withLollipop(action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        action()
    }
}
