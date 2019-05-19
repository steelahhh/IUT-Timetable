package com.alefimenko.iuttimetable.data.local

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.alefimenko.iuttimetable.data.local.Constants.ITEM_DOESNT_EXIST

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

// TODO create an interface for the preferences
@SuppressLint("ApplySharedPref")
class Preferences(private val prefs: SharedPreferences) {

    var isNightMode: Boolean
        get() = prefs.getBoolean(NIGHT_MODE, true)
        set(value) {
            prefs.edit().putBoolean(NIGHT_MODE, value).apply()
        }

    var firstLaunch: Boolean
        get() = prefs.getBoolean(FIRST_LAUNCH, true)
        set(value) {
            prefs.edit().putBoolean(FIRST_LAUNCH, value).apply()
        }

    var versionCode: Int
        get() = prefs.getInt(VERSION_CODE, ITEM_DOESNT_EXIST)
        set(value) {
            prefs.edit().putInt(VERSION_CODE, value).apply()
        }

    var switchWeek: Boolean
        get() = prefs.getBoolean(SWITCH_TO_WEEK, true)
        set(value) {
            // use commit() instead of apply(), because we exit out of the preferences too quick
            // which will prevent apply() to finish
            prefs.edit().putBoolean(SWITCH_TO_WEEK, value).commit()
        }

    var switchDay: Boolean
        get() = prefs.getBoolean(SWITCH_TO_DAY, true)
        set(value) {
            prefs.edit().putBoolean(SWITCH_TO_DAY, value).commit()
        }

    var currentGroup: Int
        get() = prefs.getInt(CURRENT_GROUP_ID, ITEM_DOESNT_EXIST)
        set(value) {
            prefs.edit().putInt(CURRENT_GROUP_ID, value).apply()
        }

    fun clearPreferences() {
        prefs.edit().clear().apply()
    }

    companion object {
        const val SCHEDULE_INFO = "SCHEDULE_INFO"
        const val NIGHT_MODE = "THEME_MODE"
        const val FIRST_LAUNCH = "PREF_KEY_IS_FIRST_LAUNCH"
        const val SWITCH_TO_WEEK = "PREF_KEY_SWITCH_TO_WEEK"
        const val SWITCH_TO_DAY = "PREF_KEY_SWITCH_TO_DAY"
        const val CURRENT_GROUP_ID = "PREF_KEY_CURRENT_GROUP_ID"
        const val VERSION_CODE = "version_code"
    }
}
