package com.alefimenko.iuttimetable.feature

import androidx.appcompat.app.AppCompatDelegate
import com.alefimenko.iuttimetable.Screens
import com.alefimenko.iuttimetable.data.local.Constants.ITEM_DOESNT_EXIST
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.navigation.Navigator
import com.bluelinelabs.conductor.Router

/*
 * Created by Alexander Efimenko on 2019-04-05.
 */

class RootFeature(
    private val sharedPreferences: Preferences,
    private val navigator: Navigator
) {

    fun bindNavigator(router: Router) {
        navigator.bind(router)
    }

    fun setRootScreen() {
        val root = when (sharedPreferences.currentGroup) {
            ITEM_DOESNT_EXIST -> Screens.PickInstituteScreen
            else -> Screens.ScheduleScreen()
        }
        navigator.setRoot(root)
    }

    fun updateTheme() {
        if (sharedPreferences.isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
