package com.alefimenko.iuttimetable.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
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
class IUTApplication : Application() {

    lateinit var refWatcher: RefWatcher

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
        if (BuildConfig.DEBUG) {
            registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                    if (activity is FragmentActivity) {
                        activity
                            .supportFragmentManager
                            .registerFragmentLifecycleCallbacks(
                                object : FragmentManager.FragmentLifecycleCallbacks() {
                                    override fun onFragmentDestroyed(
                                        fm: FragmentManager,
                                        f: Fragment
                                    ) {
                                        refWatcher.watch(f)
                                    }
                                },
                                true
                            )
                    }
                }

                override fun onActivityDestroyed(activity: Activity) {
                    refWatcher.watch(activity)
                }

                override fun onActivityPaused(activity: Activity) = Unit
                override fun onActivityResumed(activity: Activity) = Unit
                override fun onActivityStarted(activity: Activity) = Unit
                override fun onActivityStopped(activity: Activity) = Unit
                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle
                ) = Unit
            })
        }
    }
}
