package com.alefimenko.iuttimetable.pick_group.mapper

import com.alefimenko.iuttimetable.pick_group.PickGroup.Output
import com.alefimenko.iuttimetable.pick_group.feature.PickGroupFeature.News

internal object NewsToOutput : (News) -> Output? {
    override fun invoke(news: News): Output? = when (news) {
        is News.GoBack -> Output.GoBack
        is News.RouteToSchedule -> Output.GoToSchedule(news.groupInfo)
    }
}
