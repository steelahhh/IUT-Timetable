package com.alefimenko.iuttimetable.presentation.ribs.settings.mapper

import com.alefimenko.iuttimetable.presentation.ribs.settings.Settings.Output
import com.alefimenko.iuttimetable.presentation.ribs.settings.feature.SettingsFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? = when (news) {
        News.GoBack -> Output.GoBack
        else -> null
    }
}
