package com.alefimenko.iuttimetable.settings.mapper

import android.os.Build
import com.alefimenko.iuttimetable.settings.R
import com.alefimenko.iuttimetable.settings.SettingsView.ViewModel
import com.alefimenko.iuttimetable.settings.data.SettingsItem
import com.alefimenko.iuttimetable.settings.data.SettingsItemKey
import com.alefimenko.iuttimetable.settings.feature.SettingsFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel = ViewModel(state.buildSection())

    private fun State.buildSection() = listOfNotNull(
        // SettingsItem(
        //     key = SettingsItemKey.Language,
        //     titleRes = R.string.settings_language_title,
        //     switcherVisible = false
        // ),
        SettingsItem(
            key = SettingsItemKey.DarkTheme,
            titleRes = R.string.settings_theme_title,
            isChecked = isDarkTheme,
            switcherVisible = true
        ).takeUnless { Build.VERSION.SDK_INT == Build.VERSION_CODES.Q },
        SettingsItem(
            key = SettingsItemKey.RelevantSchedule,
            titleRes = R.string.settings_relevant_schedule_title,
            subtitleRes = R.string.settings_relevant_schedule_subtitle,
            isChecked = alwaysRelevantSchedule,
            switcherVisible = true
        ),
        SettingsItem(
            key = SettingsItemKey.WeekCountDown,
            titleRes = R.string.settings_week_countdown_title,
            subtitleRes = R.string.settings_week_countdown_subtitle,
            iconRes = R.drawable.ic_pick_date,
            isChecked = changeWeekCountdown,
            switcherVisible = true
        ),
        SettingsItem(
            key = SettingsItemKey.UpdateSchedule,
            titleRes = R.string.settings_update_schedule_title,
            iconRes = R.drawable.ic_refresh,
            switcherVisible = false
        ),
        SettingsItem(
            key = SettingsItemKey.Feedback,
            titleRes = R.string.settings_feedback_title,
            switcherVisible = false,
            iconRes = R.drawable.ic_mail_outline
        ),
        SettingsItem(
            key = SettingsItemKey.About,
            titleRes = R.string.settings_about_title,
            switcherVisible = false,
            iconRes = R.drawable.ic_info_outline
        )
    )
}
