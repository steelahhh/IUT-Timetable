package com.alefimenko.iuttimetable.core

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.alefimenko.iuttimetable.BuildConfig
import com.alefimenko.iuttimetable.core.di.DaggerMainComponent
import com.alefimenko.iuttimetable.core.di.MainComponent
import com.alefimenko.iuttimetable.core.di.modules.ContextModule
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Suppress("unused")
class IUTApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        MainComponent.instance = DaggerMainComponent.builder()
            .contextModule(ContextModule(applicationContext))
            .build()

        setup()
    }

    private fun setup() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}