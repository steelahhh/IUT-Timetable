package com.alefimenko.iuttimetable

import androidx.multidex.MultiDexApplication
import com.alefimenko.iuttimetable.common.RussianLocale
import com.alefimenko.iuttimetable.di.ApplicationComponent
import com.alefimenko.iuttimetable.di.DaggerApplicationComponent
import com.alefimenko.iuttimetable.di.InjectorProvider
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.soywiz.klock.KlockLocale
import io.fabric.sdk.android.Fabric
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
        val core = CrashlyticsCore.Builder()
            .disabled(BuildConfig.DEBUG)
            .build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())
    }

    private fun initializeKlock() {
        KlockLocale.default = RussianLocale
    }
}
