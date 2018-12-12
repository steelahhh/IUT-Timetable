package com.alefimenko.iuttimetable.core.data.models

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

data class ScheduleEntity(
    val semester: String = "",
    val weeks: List<String> = mutableListOf()
//    private val schedule: Map<Int, WeekSchedule> = mapOf()
)