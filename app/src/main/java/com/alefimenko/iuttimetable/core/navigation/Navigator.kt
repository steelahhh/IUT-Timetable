package com.alefimenko.iuttimetable.core.navigation

import androidx.navigation.NavController
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupFragment
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi

/*
 * Created by Alexander Efimenko on 2019-03-01.
 */

class Navigator {

    private var navController: NavController? = null

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun openPickInstitute() {
        if (navController?.currentDestination?.id == R.id.pickInstituteFragment) return
        navController?.navigate(R.id.pickInstituteFragment)
    }

    fun openPickGroup(form: Int, institute: InstituteUi) {
        if (navController?.currentDestination?.id == R.id.pickGroupFragment) return
        navController?.navigate(
            R.id.pickGroupFragment,
            PickGroupFragment.createBundle(form, institute)
        )
    }

    fun unbind() {
        navController = null
    }
}
