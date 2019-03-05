package com.alefimenko.iuttimetable.feature

import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.navigation.Navigator
import com.alefimenko.iuttimetable.core.base.BaseActivity
import com.alefimenko.iuttimetable.core.data.NetworkStatusReceiver
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import org.koin.android.ext.android.inject

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class RootActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_root

    private val sharedPreferences: LocalPreferences by inject()
    private val networkStatusReceiver: NetworkStatusReceiver by inject()
    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme()
        super.onCreate(savedInstanceState)
    }

    @Suppress("DEPRECATION")
    override fun onResume() {
        super.onResume()
        navigator.bind(findNavController(R.id.nav_host_fragment))
        registerReceiver(
            networkStatusReceiver,
            IntentFilter(CONNECTIVITY_ACTION)
        )
    }

    override fun onPause() {
        unregisterReceiver(networkStatusReceiver)
        navigator.unbind()
        super.onPause()
    }

    private fun updateTheme() {
        if (sharedPreferences.isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onBackPressed() {
        if (!findNavController(R.id.nav_host_fragment).navigateUp()) {
            super.onBackPressed()
        }
    }
}
