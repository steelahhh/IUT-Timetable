package com.alefimenko.iuttimetable

import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.remote.model.Schedule
import com.alefimenko.iuttimetable.model.GroupEntity
import com.alefimenko.iuttimetable.model.ScheduleEntity
import com.alefimenko.iuttimetable.remote.model.GroupResponse
import com.google.gson.GsonBuilder

/*
 * Created by Alexander Efimenko on 2019-04-24.
 */

val ScheduleEntity.schedule: Schedule
    get() = GsonBuilder()
        .enableComplexMapKeySerialization()
        .create()
        .fromJson(scheduleStr, Schedule::class.java)

val ScheduleEntity.rawSchedule: Schedule
    get() = GsonBuilder()
        .enableComplexMapKeySerialization()
        .create()
        .fromJson(rawScheduleStr, Schedule::class.java)

fun GroupResponse.toEntity() = GroupEntity(id, name)

fun GroupResponse.toGroupUi() = GroupUi(id, name)
fun GroupResponse.toInstituteUi() = InstituteUi(id, name)
