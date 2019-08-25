package com.alefimenko.iuttimetable.presentation.di

import androidx.fragment.app.Fragment
import com.alefimenko.iuttimetable.navigation.Screen
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFragment
import com.alefimenko.iuttimetable.presentation.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteFragment
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleController
import com.alefimenko.iuttimetable.presentation.schedule.model.GroupInfo
import com.alefimenko.iuttimetable.presentation.settings.SettingsController
import ru.terrakok.cicerone.android.support.SupportAppScreen

/*
 * Created by Alexander Efimenko on 2019-05-09.
 */

object Screens {
    data class PickInstituteScreen(
        val args: PickInstituteFragment.Args = PickInstituteFragment.Args(false)
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment = PickInstituteFragment.newInstance(args)
    }

    data class PickGroupScreen(
        private val form: Int,
        private val institute: InstituteUi
    ) : SupportAppScreen() {
        override fun getFragment() =
            PickGroupFragment.newInstance(PickGroupFragment.Args(form, institute))
    }

    data class PickInstituteScreenOld(
        val isFromSchedule: Boolean = false
    ) : Screen() {
        override fun create() = SettingsController()
    }

    object SettingsScreen : Screen() {
        override fun create() = SettingsController()
    }

    class ScheduleScreen(
        private val groupInfo: GroupInfo? = null
    ) : Screen() {
        override fun create() =
            groupInfo?.let { ScheduleController.newInstance(it) } ?: ScheduleController()
    }
}
