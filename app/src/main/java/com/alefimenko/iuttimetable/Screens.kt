package com.alefimenko.iuttimetable

import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupController
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteController
import com.alefimenko.iuttimetable.feature.schedule.ScheduleController
import com.alefimenko.iuttimetable.feature.schedule.model.GroupInfo
import com.alefimenko.iuttimetable.navigation.Screen

/*
 * Created by Alexander Efimenko on 2019-05-09.
 */

object Screens {
    data class PickGroupScreen(
        private val form: Int,
        private val institute: InstituteUi
    ) : Screen() {
        override fun create() = PickGroupController.newInstance(form, institute)
    }

    object PickInstituteScreen : Screen() {
        override fun create() = PickInstituteController()
    }

    class ScheduleScreen(
        private val groupInfo: GroupInfo? = null
    ) : Screen() {
        override fun create() = groupInfo?.let { ScheduleController.newInstance(it) } ?: ScheduleController()
    }
}
