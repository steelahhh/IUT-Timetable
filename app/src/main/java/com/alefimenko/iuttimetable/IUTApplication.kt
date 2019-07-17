package com.alefimenko.iuttimetable

import androidx.multidex.MultiDexApplication
import com.alefimenko.iuttimetable.di.applicationModule
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
        initializeTimber()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}
