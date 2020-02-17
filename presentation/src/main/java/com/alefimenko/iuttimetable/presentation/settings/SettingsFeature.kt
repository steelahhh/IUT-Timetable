package com.alefimenko.iuttimetable.presentation.settings

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.BaseEffectHandler
import com.alefimenko.iuttimetable.common.action
import com.alefimenko.iuttimetable.common.consumer
import com.alefimenko.iuttimetable.common.effectHandler
import com.alefimenko.iuttimetable.common.transformer
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.remote.FeedbackService
import com.alefimenko.iuttimetable.navigation.Navigator
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleRepository
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Effect.DisplayUpdateDialog
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Effect.LoadPreferences
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Effect.NavigateBack
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Effect.OpenAboutDialog
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Effect.OpenLanguagePickerDialog
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Effect.RecreateApplication
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Effect.SendFeedback
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Effect.SendFeedbackRequest
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Effect.SettingsItemClick
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Effect.UpdateCurrentSchedule
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Event.AboutClicked
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Event.BackClicked
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Event.FeedbackClicked
import com.alefimenko.iuttimetable.presentation.settings.SettingsItemKey.DarkTheme
import com.alefimenko.iuttimetable.presentation.settings.SettingsItemKey.RelevantSchedule
import com.alefimenko.iuttimetable.presentation.settings.SettingsItemKey.UpdateSchedule
import com.alefimenko.iuttimetable.presentation.settings.SettingsItemKey.WeekCountDown
import com.spotify.mobius.First
import com.spotify.mobius.First.first
import com.spotify.mobius.Init
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update
import io.reactivex.Observable.just
import io.reactivex.ObservableTransformer
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-07-12.
 */

object SettingsFeature {

    @Parcelize
    data class Model(
        val isDarkTheme: Boolean = false,
        val alwaysRelevantSchedule: Boolean = false,
        val changeWeekCountdown: Boolean = false
    ) : Parcelable

    sealed class Event {
        object BackClicked : Event()
        object AboutClicked : Event()
        object FeedbackClicked : Event()
        data class SendFeedbackWithSchedule(val info: FeedbackService.FeedbackInfo) : Event()
        data class SettingsItemClicked(val key: SettingsItemKey) : Event()
        data class PreferencesLoaded(
            val isDarkTheme: Boolean,
            val alwaysRelevantSchedule: Boolean,
            val changeWeekCountdown: Boolean
        ) : Event()

        data class ScheduleUpdated(val updated: Boolean, val isError: Boolean) : Event()
    }

    sealed class Effect {
        object NavigateBack : Effect()
        object LoadPreferences : Effect()
        object OpenAboutDialog : Effect()
        object SendFeedbackRequest : Effect()
        data class SendFeedback(val info: FeedbackService.FeedbackInfo) : Effect()
        object OpenLanguagePickerDialog : Effect()
        object RecreateApplication : Effect()
        data class SettingsItemClick(val key: SettingsItemKey) : Effect()
        object UpdateCurrentSchedule : Effect()
        data class DisplayUpdateDialog(val updated: Boolean, val isError: Boolean) : Effect()
    }

    object SettingsInitializer : Init<Model, Effect> {
        override fun init(model: Model): First<Model, Effect> = first(
            model,
            setOf(LoadPreferences)
        )
    }

    object SettingsUpdater : Update<Model, Event, Effect> {
        override fun update(model: Model, event: Event): Next<Model, Effect> = when (event) {
            BackClicked -> dispatch(setOf(NavigateBack))
            AboutClicked -> dispatch(setOf(OpenAboutDialog))
            FeedbackClicked -> dispatch(setOf(SendFeedbackRequest))
            is Event.SettingsItemClicked -> dispatch(
                when (event.key) {
                    SettingsItemKey.Language -> setOf(OpenLanguagePickerDialog)
                    DarkTheme -> setOf(RecreateApplication, SettingsItemClick(event.key))
                    UpdateSchedule -> setOf(UpdateCurrentSchedule)
                    RelevantSchedule -> setOf(SettingsItemClick(event.key))
                    WeekCountDown -> setOf(SettingsItemClick(event.key))
                    SettingsItemKey.Feedback -> setOf(SendFeedbackRequest)
                    SettingsItemKey.About -> setOf(OpenAboutDialog)
                }
            )
            is Event.PreferencesLoaded -> next(
                model.copy(
                    isDarkTheme = event.isDarkTheme,
                    alwaysRelevantSchedule = event.alwaysRelevantSchedule,
                    changeWeekCountdown = event.changeWeekCountdown
                )
            )
            is Event.SendFeedbackWithSchedule -> dispatch(
                setOf(SendFeedback(event.info))
            )
            is Event.ScheduleUpdated -> dispatch(
                setOf(
                    DisplayUpdateDialog(
                        updated = event.updated,
                        isError = event.isError
                    )
                )
            )
        }
    }

    class SettingsEffectHandler(
        private val preferences: Preferences,
        private val navigator: Navigator,
        private val scheduleRepository: ScheduleRepository,
        private val settingsRepository: SettingsRepository,
        private val feedbackService: FeedbackService,
        private val view: SettingsViewContract
    ) : BaseEffectHandler<Effect, Event>() {
        override fun create(): ObservableTransformer<Effect, Event> = effectHandler {
            transformer(LoadPreferences::class.java) {
                just(
                    SettingsFeature.Event.PreferencesLoaded(
                        isDarkTheme = preferences.isNightMode,
                        alwaysRelevantSchedule = preferences.switchDay,
                        changeWeekCountdown = preferences.switchWeek
                    )
                )
            }
            consumer(NavigateBack::class.java) {
                navigator.exit()
            }
            action(RecreateApplication::class.java) {
                view.onThemeClick()
            }
            transformer(SendFeedbackRequest::class.java) {
                settingsRepository.getFeedbackInfo()
                    .map {
                        Event.SendFeedbackWithSchedule(it)
                    }
            }
            transformer(UpdateCurrentSchedule::class.java) {
                scheduleRepository.updateCurrentSchedule()
                    .map<Event> { Event.ScheduleUpdated(updated = it, isError = false) }
                    .onErrorReturn {
                        Event.ScheduleUpdated(updated = false, isError = true)
                    }
            }
            consumer(DisplayUpdateDialog::class.java) { (updated, isError) ->
                view.showUpdateDialog(updated, isError)
            }
            consumer(SendFeedback::class.java) { (feedbackInfo) ->
                feedbackService.sendFeedback(feedbackInfo)
            }
            action(OpenAboutDialog::class.java) {
                view.onAboutClick()
            }
            action(OpenLanguagePickerDialog::class.java) {
            }
            transformer(SettingsItemClick::class.java) { effect ->
                when (effect.key) {
                    DarkTheme -> preferences.isNightMode = !preferences.isNightMode
                    RelevantSchedule -> preferences.switchDay = !preferences.switchDay
                    WeekCountDown -> preferences.switchWeek = !preferences.switchWeek
                    else -> Unit
                }
                just(
                    SettingsFeature.Event.PreferencesLoaded(
                        isDarkTheme = preferences.isNightMode,
                        alwaysRelevantSchedule = preferences.switchDay,
                        changeWeekCountdown = preferences.switchWeek
                    )
                )
            }
        }
    }
}
