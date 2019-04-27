package com.alefimenko.iuttimetable.remote.model

/*
 * Created by Alexander Efimenko on 2019-04-16.
 */

data class ScheduleResponse(
    val rawBody: String,
    val schedule: Schedule
)
