package com.alefimenko.iuttimetable.presentation.ribs.groups.mapper

import com.alefimenko.iuttimetable.presentation.ribs.groups.Groups.Output
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? = when (news) {
        News.GoBack -> Output.Dismiss
        News.AddGroup -> Output.AddNewGroup
        else -> null
    }
}
