package com.alefimenko.iuttimetable.navigation

import com.alefimenko.iuttimetable.base.BaseController

/*
 * Created by Alexander Efimenko on 2019-05-09.
 */

abstract class Screen {
    val tag: String = javaClass.simpleName ?: "EmptyScreen"

    abstract fun create(): BaseController
}
