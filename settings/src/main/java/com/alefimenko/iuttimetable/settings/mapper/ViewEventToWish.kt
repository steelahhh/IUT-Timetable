package com.alefimenko.iuttimetable.settings.mapper

import com.alefimenko.iuttimetable.settings.SettingsView.Event
import com.alefimenko.iuttimetable.settings.data.SettingsItemKey
import com.alefimenko.iuttimetable.settings.feature.SettingsFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? = when (event) {
        Event.OnBackClick -> Wish.GoBack
        is Event.SettingsItemClicked -> when (event.key) {
            SettingsItemKey.Language -> Wish.ChangeLanguage
            SettingsItemKey.DarkTheme -> Wish.ChangeTheme
            SettingsItemKey.RelevantSchedule -> Wish.ChangeRelevantSchedule
            SettingsItemKey.UpdateSchedule -> Wish.UpdateSchedule
            SettingsItemKey.WeekCountDown -> Wish.ChangeWeekCountDown
            SettingsItemKey.Feedback -> Wish.SendFeedback
            SettingsItemKey.About -> Wish.ShowAboutDialog
        }
        else -> null
    }
}
