package com.alefimenko.iuttimetable.presentation.root

import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import android.widget.FrameLayout
import com.alefimenko.iuttimetable.base.BaseActivity
import com.alefimenko.iuttimetable.common.NetworkStatusReceiver
import com.alefimenko.iuttimetable.presentation.R
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import org.koin.android.ext.android.inject

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class RootActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_root

    private val feature: RootFeature by inject()
    private val networkStatusReceiver: NetworkStatusReceiver by inject()

    private val container by bind<FrameLayout>(R.id.container)

    private lateinit var router: Router
    private var isReceiverRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        feature.updateTheme()
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        router = Conductor.attachRouter(this, container, savedInstanceState)
        feature.bindNavigator(router)

        feature.setRootScreen()
    }

    @Suppress("DEPRECATION")
    override fun onResume() {
        super.onResume()
        registerReceiver(
            networkStatusReceiver,
            IntentFilter(CONNECTIVITY_ACTION)
        )
        isReceiverRegistered = true
    }

    override fun onPause() {
        if (isReceiverRegistered) {
            try {
                unregisterReceiver(networkStatusReceiver)
            } catch (e: Exception) {
                // Fail silently here
            }
            isReceiverRegistered = false
        }
        super.onPause()
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        feature.onDestroy()
        super.onDestroy()
    }
}
