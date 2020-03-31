package com.alefimenko.iuttimetable.data.local

import com.alefimenko.iuttimetable.data.local.model.InstituteEntity
import java.util.LinkedList

/*
 * Created by Alexander Efimenko on 2019-04-24.
 */

object Constants {
    const val PICK_GROUP_ROOT = "isRoot:pickGroupRoot"
    const val PREFS_NAME = "MyPrefsFile"
    const val EMPTY_ENTRY = "0"
    const val MAX_GROUPS_AMOUNT = 10
    const val ITEM_DOESNT_EXIST = -1

    @JvmStatic
    val institutes: List<InstituteEntity> = LinkedList<InstituteEntity>().apply {
        addAll(
            listOf(
                InstituteEntity(35, "ИСОУ"),
                InstituteEntity(36, "СТРОИН"),
                InstituteEntity(37, "АРХИД"),
                InstituteEntity(27, "ИГиН"),
                InstituteEntity(25, "ИМиБ"),
                InstituteEntity(26, "ИПТИ"),
                InstituteEntity(2, "ИТ"),
                InstituteEntity(33, "КОТИС"),
                InstituteEntity(34, "НК"),
                InstituteEntity(3, "ВИШ")
            )
        )
    }
}
