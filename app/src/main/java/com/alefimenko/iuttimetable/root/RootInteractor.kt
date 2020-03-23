package com.alefimenko.iuttimetable.root

import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.lifecycle.Lifecycle
import com.alefimenko.iuttimetable.root.feature.RootFeature
import com.alefimenko.iuttimetable.root.feature.RootFeature.News
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.core.Interactor
import io.reactivex.functions.Consumer
import timber.log.Timber

internal class RootInteractor(
    savedInstanceState: Bundle?,
    private val router: RootRouter,
    private val feature: RootFeature
) : Interactor<Nothing>(
    savedInstanceState = savedInstanceState,
    disposables = feature
) {
    private val newsConsumer = Consumer<News> {
        Timber.d("Got news $it")
        when (it) {
            is News.UpdateTheme -> updateTheme(it.isNightMode)
            News.RouteToSchedule -> router.newRoot(RootRouter.Configuration.Schedule)
            News.RouteToPickGroup -> router.newRoot(RootRouter.Configuration.PickGroup)
        }
    }

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        ribLifecycle.startStop {
            bind(feature.news to newsConsumer)
        }
    }

    private fun updateTheme(isNightMode: Boolean) {
        Timber.d("Updating theme $isNightMode")
        when (Build.VERSION.SDK_INT) {
            VERSION_CODES.Q -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            else -> if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
    }
}