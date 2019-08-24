package com.alefimenko.iuttimetable

import androidx.multidex.MultiDexApplication
import com.alefimenko.iuttimetable.di.applicationModule
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Suppress("unused")
class IUTApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initializeKoin()
        initializeTimber()
        initializeCrashlytics()
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(applicationContext)
            modules(applicationModule)
        }
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
}
