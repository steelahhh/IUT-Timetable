package com.alefimenko.iuttimetable

import androidx.multidex.MultiDexApplication
import com.alefimenko.iuttimetable.common.IUTRefWatcher
import com.alefimenko.iuttimetable.di.modules.applicationModule
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import org.koin.android.ext.android.startKoin
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Suppress("unused")
class IUTApplication : MultiDexApplication(), IUTRefWatcher {

    override var refWatcher: RefWatcher? = null

    override fun onCreate() {
        super.onCreate()
        startKoin(this, applicationModule)
        initializeTimber()
        initializeLeakCanary()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun initializeLeakCanary() {
        refWatcher = LeakCanary.install(this)
    }
}
