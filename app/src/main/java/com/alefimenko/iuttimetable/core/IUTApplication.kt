package com.alefimenko.iuttimetable.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.multidex.MultiDexApplication
import com.alefimenko.iuttimetable.BuildConfig
import com.alefimenko.iuttimetable.core.di.DaggerComponentProvider
import com.alefimenko.iuttimetable.core.di.DaggerMainComponent
import com.alefimenko.iuttimetable.core.di.MainComponent
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Suppress("unused")
class IUTApplication : MultiDexApplication(), DaggerComponentProvider {

    lateinit var refWatcher: RefWatcher

    override val component: MainComponent by lazy {
        DaggerMainComponent.builder()
            .applicationContext(applicationContext)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
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
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
                override fun onActivityStopped(activity: Activity) = Unit
            })
        }
    }

}