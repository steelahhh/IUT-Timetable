package com.alefimenko.iuttimetable.core.data

import com.alefimenko.iuttimetable.feature.schedule.model.Schedule
import com.alefimenko.iuttimetable.model.ScheduleEntity
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
