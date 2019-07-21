package com.alefimenko.iuttimetable.presentation.settings

/*
 * Created by Alexander Efimenko on 2019-07-12.
 */

interface SettingsViewContract {
    fun onThemeClick()
    fun onAboutClick()
    fun showUpdateDialog(updated: Boolean, error: Boolean)
}
