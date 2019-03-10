package com.alefimenko.iuttimetable.core.navigation

import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupFragment
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFragment
import com.alefimenko.iuttimetable.feature.schedule.ScheduleFragment
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

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
            RouterTransaction.with(
                PickInstituteFragment()
            )
        )
    }

    fun openPickGroup(form: Int, institute: InstituteUi) {
        if (!router.backstack.any { it.tag() == PickGroupFragment.TAG }) {
            router.pushController(
                RouterTransaction.with(
                    PickGroupFragment(
                        PickGroupFragment.createBundle(
                            form = form,
                            institute = institute
                        )
                    )
                ).tag(PickGroupFragment.TAG)
            )
        }
    }

    fun openSchedule(group: GroupUi) {
        router.setRoot(
            RouterTransaction.with(
                ScheduleFragment(
                    group.toString()
                )
            )
        )
    }

    fun unbind() {
        _router = null
    }
}
