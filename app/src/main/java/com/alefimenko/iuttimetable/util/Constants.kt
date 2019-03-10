package com.alefimenko.iuttimetable.util

import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.model.GroupResponse
import java.util.LinkedList

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
    val institutes: List<GroupResponse> = LinkedList<GroupResponse>().apply {
        addAll(
            listOf(
                GroupResponse(35, "ИСОУ"),
                GroupResponse(36, "СТРОИН"),
                GroupResponse(37, "АРХИД"),
                GroupResponse(27, "ИГиН"),
                GroupResponse(25, "ИМиБ"),
                GroupResponse(26, "ИПТИ"),
                GroupResponse(2, "ИТ"),
                GroupResponse(33, "КОТИС"),
                GroupResponse(34, "НК"),
                GroupResponse(3, "ВИШ")
            )
        )
    }

    @JvmStatic
    val institutesUi = institutes.map { InstituteUi.fromResponse(it) }
}
