package com.alefimenko.iuttimetable.core

import androidx.multidex.MultiDexApplication
import com.alefimenko.iuttimetable.BuildConfig
import com.alefimenko.iuttimetable.core.di.modules.applicationModule
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import org.koin.android.ext.android.startKoin
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Suppress("unused")
class IUTApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, applicationModule)
        setup()
        initializeLeakCanary()
    }

    private fun setup() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initializeLeakCanary() {
        refWatcher = LeakCanary.install(this)
    }

    companion object {
        @JvmStatic
        var refWatcher: RefWatcher? = null
    }
}
