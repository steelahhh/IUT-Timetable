package com.alefimenko.iuttimetable.util

import com.alefimenko.iuttimetable.core.data.models.GroupEntity
import java.util.*

/**
 * IUT Timetable
 * Copyright (C) 2018  Alexander Efimenko
 * Email: a.efimenko72@gmail.com
 * Github: github.com/steelahhh
 */

object Constants {

    const val EMPTY_ENTRY = "0"

    const val MAX_GROUPS_AMOUNT = 10
    const val ITEM_DOESNT_EXIST = -1

    @JvmStatic
    val institutes: List<GroupEntity> = LinkedList<GroupEntity>().apply {
        addAll(
            listOf(
                GroupEntity(35, "ИСОУ"),
                GroupEntity(36, "СТРОИН"),
                GroupEntity(37, "АРХИД"),
                GroupEntity(27, "ИГиН"),
                GroupEntity(25, "ИМиБ"),
                GroupEntity(26, "ИПТИ"),
                GroupEntity(2, "ИТ"),
                GroupEntity(33, "КОТИС"),
                GroupEntity(34, "НК"),
                GroupEntity(3, "ВИШ")
            )
        )
    }

    object PreferenceKeys {

        const val PREFS_NAME = "MyPrefsFile"

        const val SCHEDULE_INFO = "SCHEDULE_INFO"
        const val FORM_ID = "FORM_ID"
        const val GROUP_ID = "GROUP_ID"
        const val GROUP_NAME = "GROUP_NAME"
        const val INSTITUTE_ID = "INSTITUTE_ID"
        const val INSTITUTE_NAME = "INSTITUTE_NAME"
        const val THEME_MODE = "THEME_MODE"
    }
}
