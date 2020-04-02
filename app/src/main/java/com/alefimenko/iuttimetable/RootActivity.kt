package com.alefimenko.iuttimetable

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.ViewGroup
import com.alefimenko.iuttimetable.di.component
import com.alefimenko.iuttimetable.extension.updateNavigationColor
import com.alefimenko.iuttimetable.root.builder.RootBuilder
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Node

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class RootActivity : RibActivity() {
    private val networkStatusReceiver by lazy { component.networkStatusReceiver }
    private var isReceiverRegistered = false

    override val rootViewGroup: ViewGroup get() = findViewById(R.id.container)

    override fun createRib(
        savedInstanceState: Bundle?
    ): Node<*> = RootBuilder(component).build(savedInstanceState)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) updateNavigationColor()
    }

    @Suppress("DEPRECATION")
    override fun onResume() {
        super.onResume()
        registerReceiver(
            networkStatusReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
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
}
