package com.alefimenko.iuttimetable.presentation.ribs.settings.mapper

import com.alefimenko.iuttimetable.presentation.ribs.settings.SettingsView.Event
import com.alefimenko.iuttimetable.presentation.ribs.settings.feature.SettingsFeature.Wish
import com.alefimenko.iuttimetable.presentation.settings.SettingsItemKey

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
