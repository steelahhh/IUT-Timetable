package com.alefimenko.iuttimetable.presentation.root

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.data.local.Constants.ITEM_DOESNT_EXIST
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.navigation.Navigator
import com.alefimenko.iuttimetable.presentation.BuildConfig
import com.alefimenko.iuttimetable.presentation.di.Screens
import com.bluelinelabs.conductor.Router
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

/*
 * Created by Alexander Efimenko on 2019-04-05.
 */

class RootFeature(
    private val sharedPreferences: Preferences,
    private val groupsDao: GroupsDao,
    private val navigator: Navigator
) {

    private val cd = CompositeDisposable()

    fun bindNavigator(router: Router) {
        navigator.bind(router)
    }

    fun setRootScreen() {
        val currentVersion = BuildConfig.VERSION_CODE

        cd += groupsDao.groups
            .ioMainSchedulers()
            .subscribe({ groups ->
                val hasNoGroup = groups.isEmpty() || sharedPreferences.currentGroup == ITEM_DOESNT_EXIST
                val root = when {
                    hasNoGroup -> Screens.PickInstituteScreen()
                    else -> Screens.ScheduleScreen()
                }
                navigator.setRoot(root)
            }, {
                it.printStackTrace()
            })

        sharedPreferences.versionCode = currentVersion
    }

    fun updateTheme() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            if (sharedPreferences.isNightMode) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
    }

    fun onDestroy() = cd.dispose()
}
