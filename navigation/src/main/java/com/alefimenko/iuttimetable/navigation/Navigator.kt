package com.alefimenko.iuttimetable.navigation

import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler

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

    fun unbind() {
        _router = null
    }
}
