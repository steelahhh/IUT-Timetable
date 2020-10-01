package com.alefimenko.iuttimetable

import androidx.multidex.MultiDexApplication
import com.alefimenko.iuttimetable.common.RussianLocale
import com.alefimenko.iuttimetable.di.ApplicationComponent
import com.alefimenko.iuttimetable.di.DaggerApplicationComponent
import com.alefimenko.iuttimetable.di.InjectorProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.soywiz.klock.KlockLocale
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Suppress("unused")
class IUTApplication : MultiDexApplication(), InjectorProvider {

    override val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        initializeTimber()
        initializeCrashlytics()
        initializeKlock()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun initializeCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }

    private fun initializeKlock() {
        KlockLocale.default = RussianLocale
    }
}
