package com.alefimenko.iuttimetable.schedule.mapper

import com.alefimenko.iuttimetable.schedule.Schedule.Output
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? = null
}
