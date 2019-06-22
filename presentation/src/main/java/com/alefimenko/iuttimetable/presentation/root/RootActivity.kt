package com.alefimenko.iuttimetable.presentation.root

import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import android.widget.FrameLayout
import com.alefimenko.iuttimetable.common.NetworkStatusReceiver
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.alefimenko.iuttimetable.base.BaseActivity
import com.alefimenko.iuttimetable.presentation.R
import org.koin.android.ext.android.inject

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class RootActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_root

    private val interactor: RootFeature by inject()
    private val networkStatusReceiver: NetworkStatusReceiver by inject()

    private val container by bind<FrameLayout>(R.id.container)

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        interactor.updateTheme()
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        router = Conductor.attachRouter(this, container, savedInstanceState)
        interactor.bindNavigator(router)

        interactor.setRootScreen()
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

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
