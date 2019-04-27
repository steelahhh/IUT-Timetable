package com.alefimenko.iuttimetable.core

import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.alefimenko.iuttimetable.base.BaseController

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

abstract class RefWatchingController<Event, ViewModel> : BaseController<Event, ViewModel>() {
    private var hasExited: Boolean = false

    override fun onDestroy() {
        super.onDestroy()
        if (hasExited) {
            IUTApplication.refWatcher?.watch(this)
        }
    }

    override fun onChangeEnded(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super.onChangeEnded(changeHandler, changeType)

        hasExited = !changeType.isEnter
        if (isDestroyed) {
            IUTApplication.refWatcher?.watch(this)
        }
    }
}
