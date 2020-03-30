package com.alefimenko.iuttimetable.presentation.ribs.settings.feature

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.common.extension.justOnMain
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.remote.FeedbackService
import com.alefimenko.iuttimetable.presentation.ribs.settings.builder.SettingsScope
import com.alefimenko.iuttimetable.presentation.ribs.settings.feature.SettingsFeature.Action
import com.alefimenko.iuttimetable.presentation.ribs.settings.feature.SettingsFeature.Action.Execute
import com.alefimenko.iuttimetable.presentation.ribs.settings.feature.SettingsFeature.Effect
import com.alefimenko.iuttimetable.presentation.ribs.settings.feature.SettingsFeature.News
import com.alefimenko.iuttimetable.presentation.ribs.settings.feature.SettingsFeature.State
import com.alefimenko.iuttimetable.presentation.ribs.settings.feature.SettingsFeature.Wish
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleRepository
import com.alefimenko.iuttimetable.presentation.settings.SettingsRepository
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.PostProcessor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import io.reactivex.Observable
import io.reactivex.Observable.empty
import javax.inject.Inject
import kotlinx.android.parcel.Parcelize

@SettingsScope
internal class SettingsFeature @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val settingsRepository: SettingsRepository,
    private val feedbackService: FeedbackService,
    private val preferences: Preferences
) : BaseFeature<Wish, Action, Effect, State, News>(
    initialState = State(),
    wishToAction = { Execute(it) },
    bootstrapper = BootStrapperImpl(),
    actor = ActorImpl(
        settingsRepository = settingsRepository,
        scheduleRepository = scheduleRepository,
        feedbackService = feedbackService,
        preferences = preferences
    ),
    reducer = ReducerImpl(),
    postProcessor = PostProcessorImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    @Parcelize
    data class State(
        val isDarkTheme: Boolean = false,
        val alwaysRelevantSchedule: Boolean = false,
        val changeWeekCountdown: Boolean = false
    ) : Parcelable

    sealed class Wish {
        object GoBack : Wish()
        object ChangeTheme : Wish()
        object ChangeRelevantSchedule : Wish()
        object ChangeWeekCountDown : Wish()
        object ChangeLanguage : Wish()
        object UpdateSchedule : Wish()
        object SendFeedback : Wish()
        object ShowAboutDialog : Wish()
    }

    sealed class Action {
        data class Execute(val wish: Wish) : Action()
        object FetchSettings : Action()
    }

    sealed class Effect {
        object NoOp : Effect()
        object ThemeChanged : Effect()
        object GoBack : Effect()
        object SettingsChanged : Effect()
        data class PreferencesLoaded(
            val isDarkTheme: Boolean,
            val alwaysRelevantSchedule: Boolean,
            val changeWeekCountdown: Boolean
        ) : Effect()
        object OpenAboutDialog : Effect()
        object ScheduleUpdated : Effect()
        object ScheduleUpdateFailed : Effect()
        object ScheduleUpdateNotNecessary : Effect()
    }

    sealed class News {
        object GoBack : News()
        object ShowAboutDialog : News()
        object ShowSuccessDialog : News()
        object ShowErrorDialog : News()
        object ShowUnnecessaryDialog : News()
        object RestartApplication : News()
    }

    class BootStrapperImpl : Bootstrapper<Action> {
        override fun invoke(): Observable<Action> = justOnMain(Action.FetchSettings)
    }

    class ActorImpl(
        private val settingsRepository: SettingsRepository,
        private val feedbackService: FeedbackService,
        private val scheduleRepository: ScheduleRepository,
        private val preferences: Preferences
    ) : Actor<State, Action, Effect> {
        override fun invoke(state: State, action: Action): Observable<Effect> = when (action) {
            is Execute -> handleWish(action.wish)
            is Action.FetchSettings -> justOnMain(
                Effect.PreferencesLoaded(
                    isDarkTheme = preferences.isNightMode,
                    alwaysRelevantSchedule = preferences.switchDay,
                    changeWeekCountdown = preferences.switchWeek
                )
            )
        }

        private fun handleWish(wish: Wish): Observable<Effect> = when (wish) {
            Wish.GoBack -> justOnMain(Effect.GoBack)
            Wish.ChangeRelevantSchedule -> {
                preferences.switchDay = !preferences.switchDay
                justOnMain(Effect.SettingsChanged)
            }
            Wish.ChangeWeekCountDown -> {
                preferences.switchWeek = !preferences.switchWeek
                justOnMain(Effect.SettingsChanged)
            }
            Wish.ChangeTheme -> {
                preferences.isNightMode = !preferences.isNightMode
                justOnMain(Effect.ThemeChanged)
            }
            Wish.ChangeLanguage -> empty()
            Wish.UpdateSchedule -> scheduleRepository.updateCurrentSchedule()
                .ioMainSchedulers()
                .map { isScheduleUpdated ->
                    if (isScheduleUpdated) Effect.ScheduleUpdated else Effect.ScheduleUpdateNotNecessary
                }
                .onErrorReturnItem(Effect.ScheduleUpdateFailed)
            Wish.SendFeedback -> settingsRepository.getFeedbackInfo()
                .ioMainSchedulers()
                .map { feedbackInfo ->
                    feedbackService.sendFeedback(feedbackInfo)
                    Effect.NoOp
                }
            Wish.ShowAboutDialog -> justOnMain(Effect.OpenAboutDialog)
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.PreferencesLoaded -> state.copy(
                isDarkTheme = effect.isDarkTheme,
                changeWeekCountdown = effect.changeWeekCountdown,
                alwaysRelevantSchedule = effect.alwaysRelevantSchedule
            )
            else -> state
        }
    }

    class PostProcessorImpl : PostProcessor<Action, Effect, State> {
        override fun invoke(action: Action, effect: Effect, state: State): Action? = when (effect) {
            is Effect.SettingsChanged -> Action.FetchSettings
            else -> null
        }
    }

    class NewsPublisherImpl : NewsPublisher<Action, Effect, State, News> {
        override fun invoke(action: Action, effect: Effect, state: State): News? = when (effect) {
            is Effect.ThemeChanged -> News.RestartApplication
            is Effect.GoBack -> News.GoBack
            is Effect.OpenAboutDialog -> News.ShowAboutDialog
            is Effect.ScheduleUpdated -> News.ShowSuccessDialog
            is Effect.ScheduleUpdateNotNecessary -> News.ShowUnnecessaryDialog
            is Effect.ScheduleUpdateFailed -> News.ShowErrorDialog
            else -> null
        }
    }
}
