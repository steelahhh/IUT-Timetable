package com.alefimenko.iuttimetable.schedule.mapper

import com.alefimenko.iuttimetable.schedule.Schedule.Input
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? = when (event) {
        is Input.DownloadSchedule -> Wish.DownloadSchedule(event.groupInfo)
        is Input.LoadCurrentSchedule -> Wish.LoadSchedule
    }
}
