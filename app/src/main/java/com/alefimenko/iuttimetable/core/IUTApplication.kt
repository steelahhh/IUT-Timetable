package com.alefimenko.iuttimetable.core

import androidx.multidex.MultiDexApplication
import com.alefimenko.iuttimetable.BuildConfig
import com.alefimenko.iuttimetable.core.di.DaggerComponentProvider
import com.alefimenko.iuttimetable.core.di.DaggerMainComponent
import com.alefimenko.iuttimetable.core.di.MainComponent
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Suppress("unused")
class IUTApplication: MultiDexApplication(), DaggerComponentProvider {

    override val component: MainComponent by lazy {
        DaggerMainComponent.builder()
            .applicationContext(applicationContext)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        setup()
    }

    private fun setup() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}