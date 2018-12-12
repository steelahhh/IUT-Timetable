package com.alefimenko.iuttimetable.core

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.alefimenko.iuttimetable.BuildConfig
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Suppress("unused")
class IUTApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}