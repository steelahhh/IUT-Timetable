package com.alefimenko.iuttimetable.groups.mapper

import com.alefimenko.iuttimetable.groups.Groups.Output
import com.alefimenko.iuttimetable.groups.feature.GroupsFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? = when (news) {
        is News.GoBack -> Output.Dismiss
        is News.UpdateNewGroup -> Output.UpdateGroup
        is News.AddGroup -> Output.AddNewGroup(isRoot = false)
        is News.LastGroupDeleted -> Output.AddNewGroup(isRoot = true)
        else -> null
    }
}
