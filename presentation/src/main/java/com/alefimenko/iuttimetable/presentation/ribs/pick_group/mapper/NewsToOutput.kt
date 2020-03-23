package com.alefimenko.iuttimetable.presentation.ribs.pick_group.mapper

import com.alefimenko.iuttimetable.presentation.ribs.pick_group.PickGroup.Output
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? =
        TODO("Implement PickGroupNewsToOutput mapping")
}
