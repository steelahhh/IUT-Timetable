package com.alefimenko.iuttimetable.presentation.ribs.settings

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.alefimenko.iuttimetable.presentation.ribs.settings.analytics.SettingsAnalytics
import com.alefimenko.iuttimetable.presentation.ribs.settings.feature.SettingsFeature
import com.alefimenko.iuttimetable.presentation.ribs.settings.feature.SettingsFeature.News
import com.alefimenko.iuttimetable.presentation.ribs.settings.mapper.NewsToOutput
import com.alefimenko.iuttimetable.presentation.ribs.settings.mapper.StateToViewModel
import com.alefimenko.iuttimetable.presentation.ribs.settings.mapper.ViewEventToAnalyticsEvent
import com.alefimenko.iuttimetable.presentation.ribs.settings.mapper.ViewEventToWish
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import io.reactivex.Observable.wrap
import io.reactivex.functions.Consumer

internal class SettingsInteractor(
    savedInstanceState: Bundle?,
    private val router: SettingsRouter,
    private val output: Consumer<Settings.Output>,
    private val feature: SettingsFeature
) : Interactor<SettingsView>(
    savedInstanceState = savedInstanceState,
    disposables = feature
) {

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        ribLifecycle.createDestroy {
            bind(feature.news to output using NewsToOutput)
        }
    }

    override fun onViewCreated(view: SettingsView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature.news to Consumer<News> {
                when (it) {
                    News.RestartApplication -> view.onThemeClick()
                    News.ShowAboutDialog -> view.onAboutClick()
                    News.ShowSuccessDialog -> view.showUpdateDialog(true)
                    News.ShowErrorDialog -> view.showErrorDialog()
                    News.ShowUnnecessaryDialog -> view.showUpdateDialog(false)
                }
            })
            bind(wrap(feature).distinctUntilChanged() to view using StateToViewModel)
            bind(view to feature using ViewEventToWish)
            bind(view to SettingsAnalytics using ViewEventToAnalyticsEvent)
        }
    }
}
