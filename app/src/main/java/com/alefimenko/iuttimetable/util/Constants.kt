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
    const val PREFS_NAME = "MyPrefsFile"

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

}
