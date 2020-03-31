package com.alefimenko.iuttimetable.presentation.ribs.schedule.mapper

import com.alefimenko.iuttimetable.presentation.ribs.schedule.Schedule.Input
import com.alefimenko.iuttimetable.presentation.ribs.schedule.feature.ScheduleFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? = when (event) {
        is Input.DownloadSchedule -> Wish.DownloadSchedule(event.groupInfo)
        is Input.LoadCurrentSchedule -> Wish.LoadSchedule
    }
}
