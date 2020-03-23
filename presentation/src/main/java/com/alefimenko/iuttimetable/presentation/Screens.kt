package com.alefimenko.iuttimetable.presentation

import com.alefimenko.iuttimetable.data.GroupInfo
import com.alefimenko.iuttimetable.data.Institute
import com.alefimenko.iuttimetable.navigation.Screen
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupController
import com.alefimenko.iuttimetable.presentation.pickinstitute.PickInstituteController
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleController
import com.alefimenko.iuttimetable.presentation.settings.SettingsController

/*
 * Created by Alexander Efimenko on 2019-05-09.
 */

object Screens {
    data class PickGroupScreen(
        private val form: Int,
        private val institute: Institute
    ) : Screen() {
        override fun create() = PickGroupController.newInstance(form, institute)
    }

    data class PickInstituteScreen(
        val isFromSchedule: Boolean = false
    ) : Screen() {
        override fun create() = PickInstituteController.newInstance(isFromSchedule)
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