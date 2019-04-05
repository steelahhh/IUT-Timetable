package com.alefimenko.iuttimetable.core.navigation

import com.alefimenko.iuttimetable.core.base.BaseController
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupController
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteController
import com.alefimenko.iuttimetable.feature.schedule.ScheduleController
import com.alefimenko.iuttimetable.feature.schedule.model.GroupInfo
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 2019-03-01.
 */

class Navigator {

    private var _router: Router? = null
    private val router: Router get() = _router ?: error("Router not bound")

    fun bind(navController: Router) {
        this._router = navController
    }

    fun openPickInstitute() {
        router.setRoot(
            controller = PickInstituteController(),
            tag = PickInstituteController.TAG
        )
    }

    fun openPickGroup(form: Int, institute: InstituteUi) {
        router.safePush(
            controller = PickGroupController.newInstance(
                form = form,
                institute = institute
            ),
            tag = PickGroupController.TAG
        )
    }

    fun openSchedule(groupInfo: GroupInfo) {
        router.setRoot(
            controller = ScheduleController.newInstance(groupInfo),
            tag = ScheduleController.TAG
        )
    }

    fun unbind() {
        _router = null
    }
}

private fun Router.safePush(
    controller: BaseController<*, *>,
    tag: String,
    changeHandler: ControllerChangeHandler = HorizontalChangeHandler()
) = if (!backstack.any { it.tag() == tag }) {
    push(controller, tag, changeHandler)
} else {
    Timber.d("Controller is already pushed into the backstack")
}

private fun Router.push(
    controller: BaseController<*, *>,
    tag: String,
    changeHandler: ControllerChangeHandler = HorizontalChangeHandler()
) = pushController(
    RouterTransaction
        .with(controller)
        .tag(tag)
        .pushChangeHandler(changeHandler)
        .popChangeHandler(changeHandler)
)

private fun Router.setRoot(
    controller: BaseController<*, *>,
    tag: String,
    changeHandler: ControllerChangeHandler = HorizontalChangeHandler()
) = setRoot(
    RouterTransaction
        .with(controller)
        .tag(tag)
        .pushChangeHandler(changeHandler)
        .popChangeHandler(changeHandler)
)
