package com.alefimenko.iuttimetable.pick_institute.mapper

import com.alefimenko.iuttimetable.pick_institute.PickInstitute.Output
import com.alefimenko.iuttimetable.pick_institute.feature.PickInstituteFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? = when (news) {
        is News.RouteToPickGroup -> Output.RouteToPickInstitute(news.form, news.institute)
    }
}
