package com.alefimenko.iuttimetable.root

import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.lifecycle.Lifecycle
import com.alefimenko.iuttimetable.presentation.ribs.pick_group_root.PickGroupRoot
import com.alefimenko.iuttimetable.presentation.ribs.schedule.Schedule
import com.alefimenko.iuttimetable.root.RootRouter.Configuration
import com.alefimenko.iuttimetable.root.feature.RootFeature
import com.alefimenko.iuttimetable.root.feature.RootFeature.News
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.core.Interactor
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

internal class RootInteractor(
    savedInstanceState: Bundle?,
    private val router: RootRouter,
    private val feature: RootFeature
) : Interactor<Nothing>(
    savedInstanceState = savedInstanceState,
    disposables = feature
) {
    val scheduleInput = PublishSubject.create<Schedule.Input>()

    val scheduleOutput = Consumer<Schedule.Output> { output ->
        when (output) {
            is Schedule.Output.OpenPickGroup -> if (output.isRoot)
                router.newRoot(Configuration.PickGroup(isRoot = output.isRoot))
            else
                router.push(Configuration.PickGroup(isRoot = output.isRoot))
            is Schedule.Output.GroupUpdated -> scheduleInput.onNext(Schedule.Input.LoadCurrentSchedule)
        }
    }

    val pickGroupNavigator: Consumer<PickGroupRoot.Output> = Consumer {
        when (it) {
            is PickGroupRoot.Output.OpenSchedule -> {
                router.newRoot(Configuration.Schedule)
                scheduleInput.onNext(Schedule.Input.DownloadSchedule(it.groupInfo))
            }
            PickGroupRoot.Output.GoBack -> router.popBackStack()
        }
    }

    private val newsConsumer = Consumer<News> {
        when (it) {
            is News.UpdateTheme -> updateTheme(it.isNightMode)
            News.RouteToSchedule -> {
                router.newRoot(Configuration.Schedule)
                scheduleInput.onNext(Schedule.Input.LoadCurrentSchedule)
            }
            News.RouteToPickGroup -> router.newRoot(Configuration.PickGroup(isRoot = true))
        }
    }

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
        ribLifecycle.startStop {
            bind(feature.news to newsConsumer)
        }
    }

    private fun updateTheme(isNightMode: Boolean) {
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
