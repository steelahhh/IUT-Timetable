package com.alefimenko.iuttimetable.navigation

import com.alefimenko.iuttimetable.base.BaseController
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 2019-03-01.
 */

class Navigator {

    private var _router: Router? = null
    private val router: Router
        get() = _router ?: error("Router not bound")

    fun bind(navController: Router) {
        _router = navController
    }

    fun setRoot(screen: Screen) {
        if (!router.hasRootController()) {
            router.setRoot(
                controller = screen.create(),
                tag = screen.tag,
                changeHandler = SimpleSwapChangeHandler()
            )
        }
    }

    fun push(screen: Screen) = router.safePush(
        controller = screen.create(),
        tag = screen.tag
    )

    fun replace(screen: Screen) = router.setRoot(
        controller = screen.create(),
        tag = screen.tag
    )

    fun exit() = router.popCurrentController()

    fun unbind() {
        _router = null
    }

    private fun Router.setRoot(
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

    private fun Router.safePush(
        controller: BaseController,
        tag: String,
        changeHandler: ControllerChangeHandler = HorizontalChangeHandler()
    ) = if (!backstack.any { it.tag() == tag }) {
        push(controller, tag, changeHandler)
    } else {
        Timber.d("Controller is already pushed into the backstack")
    }

    private fun Router.push(
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

}
