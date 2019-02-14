package com.alefimenko.iuttimetable.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseActivity
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupFragment
import org.koin.android.ext.android.inject

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class RootActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_root

    private val sharedPreferences: LocalPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme()
        super.onCreate(savedInstanceState)
        val fraggo = supportFragmentManager.findFragmentById(R.id.content)

        if (fraggo == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.content, PickGroupFragment.newInstance(), "pickgroup")
                .commitNow()
        }
    }

    private fun updateTheme() {
        if (sharedPreferences.isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
