package com.alefimenko.iuttimetable.presentation.root

import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import android.widget.FrameLayout
import com.alefimenko.iuttimetable.base.BaseActivity
import com.alefimenko.iuttimetable.common.NetworkStatusReceiver
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteFragment
import org.koin.android.ext.android.inject

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class RootActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_root

    private val feature: RootFeature by inject()
    private val networkStatusReceiver: NetworkStatusReceiver by inject()

    private val container by bind<FrameLayout>(R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        feature.updateTheme()
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        feature.setRootScreen()

        supportFragmentManager.beginTransaction()
            .replace(container.id, PickInstituteFragment.newInstance(
                PickInstituteFragment.Args(
                    false
                )
            ))
            .commitNow()
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

    override fun onDestroy() {
        feature.onDestroy()
        super.onDestroy()
    }
}
