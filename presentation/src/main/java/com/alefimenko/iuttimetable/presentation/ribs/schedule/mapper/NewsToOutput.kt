package com.alefimenko.iuttimetable.presentation.ribs.schedule.mapper

import com.alefimenko.iuttimetable.presentation.ribs.schedule.Schedule.Output
import com.alefimenko.iuttimetable.presentation.ribs.schedule.feature.ScheduleFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? = null
}
