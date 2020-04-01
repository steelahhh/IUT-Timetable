package com.alefimenko.iuttimetable.settings.mapper

import com.alefimenko.iuttimetable.settings.Settings.Output
import com.alefimenko.iuttimetable.settings.feature.SettingsFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? = when (news) {
        News.GoBack -> Output.GoBack
        else -> null
    }
}
