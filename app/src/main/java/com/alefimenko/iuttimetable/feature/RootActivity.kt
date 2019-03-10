package com.alefimenko.iuttimetable.feature

import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseActivity
import com.alefimenko.iuttimetable.core.data.NetworkStatusReceiver
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.core.navigation.Navigator
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFragment
import com.alefimenko.iuttimetable.feature.schedule.ScheduleFragment
import com.alefimenko.iuttimetable.util.Constants.ITEM_DOESNT_EXIST
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import org.koin.android.ext.android.inject

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class RootActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_root

    private val sharedPreferences: LocalPreferences by inject()
    private val networkStatusReceiver: NetworkStatusReceiver by inject()
    private val navigator: Navigator by inject()

    private val container by bind<FrameLayout>(R.id.nav_host_fragment)

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme()
        super.onCreate(savedInstanceState)

        router = Conductor.attachRouter(this, container, savedInstanceState)

        navigator.bind(router)
        if (!router.hasRootController()) {
            when (sharedPreferences.currentGroup) {
                ITEM_DOESNT_EXIST -> router.setRoot(RouterTransaction.with(PickInstituteFragment()))
                else -> router.setRoot(RouterTransaction.with(ScheduleFragment()))
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onResume() {
        super.onResume()
        registerReceiver(
            networkStatusReceiver,
            IntentFilter(CONNECTIVITY_ACTION)
        )
    }

    override fun onPause() {
        unregisterReceiver(networkStatusReceiver)
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
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
