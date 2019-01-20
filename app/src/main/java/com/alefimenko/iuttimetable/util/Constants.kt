package com.alefimenko.iuttimetable.util

import com.alefimenko.iuttimetable.model.GroupModel
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
    val institutes: List<GroupModel> = LinkedList<GroupModel>().apply {
        addAll(
            listOf(
                GroupModel(35, "ИСОУ"),
                GroupModel(36, "СТРОИН"),
                GroupModel(37, "АРХИД"),
                GroupModel(27, "ИГиН"),
                GroupModel(25, "ИМиБ"),
                GroupModel(26, "ИПТИ"),
                GroupModel(2, "ИТ"),
                GroupModel(33, "КОТИС"),
                GroupModel(34, "НК"),
                GroupModel(3, "ВИШ")
            )
        )
    }

}
