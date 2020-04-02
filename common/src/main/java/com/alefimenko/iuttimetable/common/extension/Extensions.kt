package com.alefimenko.iuttimetable.common.extension

import android.os.Build

/*
 * Created by Alexander Efimenko on 2019-04-27.
 */

inline fun withLollipop(action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        action()
    }
}

val Int.asGenitiveMonth get(): String {
    return when (this) {
        1 -> "января"
        2 -> "февраля"
        3 -> "марта"
        4 -> "апреля"
        5 -> "мая"
        6 -> "июня"
        7 -> "июля"
        8 -> "августа"
        9 -> "сентября"
        10 -> "октября"
        11 -> "ноября"
        12 -> "декабря"
        else -> ""
    }
}
