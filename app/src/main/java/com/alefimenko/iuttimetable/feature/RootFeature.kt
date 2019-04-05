package com.alefimenko.iuttimetable.feature

import androidx.appcompat.app.AppCompatDelegate
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.core.navigation.Navigator
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteController
import com.alefimenko.iuttimetable.feature.schedule.ScheduleController
import com.alefimenko.iuttimetable.util.Constants.ITEM_DOESNT_EXIST
import com.bluelinelabs.conductor.Router

/*
 * Created by Alexander Efimenko on 2019-04-05.
 */

class RootFeature(
    private val sharedPreferences: LocalPreferences,
    private val navigator: Navigator
) {

    fun bindNavigator(router: Router) {
        navigator.bind(router)
    }

    fun setRootScreen() {
        val rootTag = when (sharedPreferences.currentGroup) {
            ITEM_DOESNT_EXIST -> PickInstituteController.TAG
            else -> ScheduleController.TAG
        }
        navigator.setRootScreen(rootTag)
    }

    fun updateTheme() {
        if (sharedPreferences.isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
