package com.alefimenko.iuttimetable.presentation.root

import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.airbnb.mvrx.viewModel
import com.alefimenko.iuttimetable.base.BaseActivity
import com.alefimenko.iuttimetable.base.BaseFragment
import com.alefimenko.iuttimetable.presentation.R
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class RootActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_root

    private val vm by viewModel(RootViewModel::class)
    private val navigatorHolder: NavigatorHolder by inject()

    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFragment

    private val navigator =
        object : SupportAppNavigator(this, supportFragmentManager, R.id.container) {
            override fun setupFragmentTransaction(
                command: Command?,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction?
            ) {
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        vm.updateTheme()
        super.onCreate(savedInstanceState)

        vm.setRootScreen()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    @Suppress("DEPRECATION")
    override fun onResume() {
        super.onResume()
        registerReceiver(
            vm.networkStatusReceiver,
            IntentFilter(CONNECTIVITY_ACTION)
        )
    }

    override fun onBackPressed() = currentFragment?.onBackPressed() ?: super.onBackPressed()

    override fun onPause() {
        navigatorHolder.removeNavigator()
        unregisterReceiver(vm.networkStatusReceiver)
        super.onPause()
    }
}
