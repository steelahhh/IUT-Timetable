package com.alefimenko.iuttimetable.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseActivity
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.core.di.injector
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupFragment
import timber.log.Timber
import javax.inject.Inject

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class RootActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_root

    @Inject
    lateinit var sharedPreferences: LocalPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
        if (sharedPreferences.isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, PickGroupFragment.newInstance())
                .commitNow()

        Timber.d("Night mode: ${sharedPreferences.isNightMode}")
    }
}
