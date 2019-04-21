package com.alefimenko.iuttimetable.core.data.remote.model

import com.alefimenko.iuttimetable.feature.schedule.model.Schedule

/*
 * Created by Alexander Efimenko on 2019-04-16.
 */

data class ScheduleResponse(
    val rawBody: String,
    val schedule: Schedule
)
