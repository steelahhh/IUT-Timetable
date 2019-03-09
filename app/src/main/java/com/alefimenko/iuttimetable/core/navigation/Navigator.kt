package com.alefimenko.iuttimetable.core.navigation

import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupFragment
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.feature.schedule.ScheduleFragment
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

/*
 * Created by Alexander Efimenko on 2019-03-01.
 */

class Navigator {

    private var navController: Router? = null

    fun bind(navController: Router) {
        this.navController = navController
    }

    fun openPickInstitute() {
        navController?.setRoot(
            RouterTransaction.with(
                PickInstituteFragment()
            )
        )
    }

    fun openPickGroup(form: Int, institute: InstituteUi) {
        if (navController?.backstackSize ?: 1 < 2) {
            navController?.pushController(
                RouterTransaction.with(
                    PickGroupFragment(
                        PickGroupFragment.createBundle(
                            form = form,
                            institute = institute
                        )
                    )
                )
            )
        }
    }

    fun openSchedule(group: GroupUi) {
        navController?.setRoot(
            RouterTransaction.with(
                ScheduleFragment(
                    group.toString()
                )
            )
        )
    }

    fun unbind() {
        navController = null
    }
}
