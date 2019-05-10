package com.alefimenko.iuttimetable.navigation

import com.alefimenko.iuttimetable.base.BaseController
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 2019-05-09.
 */

fun Router.setRoot(
    controller: BaseController,
    tag: String,
    changeHandler: ControllerChangeHandler = HorizontalChangeHandler()
) = setRoot(
    RouterTransaction
        .with(controller)
        .tag(tag)
        .pushChangeHandler(changeHandler)
        .popChangeHandler(changeHandler)
)

fun Router.safePush(
    controller: BaseController,
    tag: String,
    changeHandler: ControllerChangeHandler = HorizontalChangeHandler()
) = if (!backstack.any { it.tag() == tag }) {
    push(controller, tag, changeHandler)
} else {
    Timber.d("Controller is already pushed into the backstack")
}

internal fun Router.push(
    controller: BaseController,
    tag: String,
    changeHandler: ControllerChangeHandler = HorizontalChangeHandler()
) = pushController(
    RouterTransaction
        .with(controller)
        .tag(tag)
        .pushChangeHandler(changeHandler)
        .popChangeHandler(changeHandler)
)
