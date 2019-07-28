package com.alefimenko.iuttimetable.presentation.schedule

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.BaseEffectHandler
import com.alefimenko.iuttimetable.common.action
import com.alefimenko.iuttimetable.common.consumer
import com.alefimenko.iuttimetable.common.effectHandler
import com.alefimenko.iuttimetable.common.transformer
import com.alefimenko.iuttimetable.data.remote.model.Schedule
import com.alefimenko.iuttimetable.navigation.Navigator
import com.alefimenko.iuttimetable.presentation.DateInteractor
import com.alefimenko.iuttimetable.presentation.di.Screens
import com.alefimenko.iuttimetable.presentation.schedule.model.GroupInfo
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
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 2019-05-10.
 */

object ScheduleFeature {

    @Parcelize
    data class Model(
        val groupInfo: GroupInfo? = null,
        val currentWeek: Int = -1,
        val selectedWeek: Int = -1,
        @Transient val currentDay: Int = 0,
        @Transient val isLoading: Boolean = false,
        @Transient val isError: Boolean = false,
        val schedule: Schedule? = null
    ) : Parcelable

    sealed class Event {
        object DownloadSchedule : Event()
        object StartedLoading : Event()
        data class ShowSchedule(
            val schedule: Schedule,
            val currentWeek: Int,
            val shouldScrollToDay: Boolean = true
        ) : Event()

        object RequestWeekChange : Event()
        object NavigateToSettings : Event()
        object NavigateToAddNewGroup : Event()
        data class SwitchWeek(val week: Int) : Event()
        data class ErrorLoading(val throwable: Throwable) : Event()
        data class ChangeClassVisibility(
            val classIndex: Int,
            val dayIndex: Int,
            val weekIndex: Int
        ) : Event()
    }

    sealed class Effect {
        object SwitchToCurrentDay : Effect()
        object DisplaySchedule : Effect()
        object OpenSettings : Effect()
        object OpenAddGroup : Effect()
        data class ChangeClassVisibility(
            val classIndex: Int,
            val dayIndex: Int,
            val weekIndex: Int
        ) : Effect()
        data class ChangeWeek(val week: Int) : Effect()
        data class DownloadSchedule(val groupInfo: GroupInfo) : Effect()
        data class OpenChangeWeekDialog(val weeks: List<String>, val selectedWeek: Int) : Effect()
    }

    object ScheduleInitializer : Init<Model, Effect> {
        override fun init(model: Model): First<Model, Effect> = first(
            model,
            if (model.groupInfo != null && model.schedule == null)
                setOf(Effect.DownloadSchedule(model.groupInfo))
            else
                setOf(Effect.DisplaySchedule)
        )
    }

    object ScheduleUpdater : Update<Model, Event, Effect> {
        override fun update(model: Model, event: Event): Next<Model, Effect> = when (event) {
            is Event.DownloadSchedule -> dispatch(
                if (model.groupInfo != null)
                    setOf(Effect.DownloadSchedule(model.groupInfo))
                else
                    setOf()
            )
            is Event.ShowSchedule -> next(
                model.copy(
                    schedule = event.schedule,
                    isLoading = false,
                    isError = false,
                    selectedWeek = event.currentWeek,
                    currentWeek = event.currentWeek
                ),
                if (event.shouldScrollToDay) {
                    setOf(Effect.SwitchToCurrentDay)
                } else {
                    setOf()
                }
            )
            Event.StartedLoading -> next(
                model.copy(
                    isLoading = true,
                    isError = false
                )
            )
            is Event.ErrorLoading -> next(
                model.copy(
                    isLoading = false,
                    isError = true
                )
            )
            is Event.RequestWeekChange -> dispatch(
                if (model.schedule?.weeks?.size == 2)
                    setOf(Effect.ChangeWeek(model.selectedWeek))
                else
                    setOf(
                        Effect.OpenChangeWeekDialog(
                            model.schedule?.weeks.orEmpty(),
                            model.selectedWeek
                        )
                    )
            )
            is Event.SwitchWeek -> next(
                model.copy(
                    isLoading = false,
                    selectedWeek = event.week
                ),
                setOf(Effect.SwitchToCurrentDay)
            )
            is Event.NavigateToSettings -> dispatch(setOf(Effect.OpenSettings))
            is Event.ChangeClassVisibility -> dispatch(
                setOf(
                    Effect.ChangeClassVisibility(
                        event.classIndex,
                        event.dayIndex,
                        event.weekIndex
                    )
                )
            )
            is Event.NavigateToAddNewGroup -> dispatch(
                setOf(Effect.OpenAddGroup)
            )
        }
    }

    class ScheduleEffectHandler(
        private val repository: ScheduleRepository,
        private val dateInteractor: DateInteractor,
        private val navigator: Navigator,
        private val view: ScheduleViewContract
    ) : BaseEffectHandler<Effect, Event>() {
        override fun create(): ObservableTransformer<Effect, Event> = effectHandler {
            transformer(Effect.DownloadSchedule::class.java) { (groupInfo) ->
                repository.downloadSchedule(groupInfo)
                    .map<Event> { schedule ->
                        Event.ShowSchedule(
                            schedule,
                            if (schedule.weeks.size == 2) dateInteractor.currentWeek else 0
                        )
                    }
                    .onErrorReturn { error ->
                        Timber.e(error)
                        Event.ErrorLoading(error)
                    }
                    .startWith(Event.StartedLoading)
            }
            transformer(Effect.ChangeClassVisibility::class.java) { (classIndex, dayIndex, weekIndex) ->
                repository.hideClassAndUpdate(classIndex, dayIndex, weekIndex)
                    .map { schedule ->
                        ScheduleFeature.Event.ShowSchedule(
                            schedule,
                            if (schedule.weeks.size == 2) dateInteractor.currentWeek else 0,
                            shouldScrollToDay = false
                        )
                    }
            }
            transformer(Effect.DisplaySchedule::class.java) {
                repository.getSchedule()
                    .map<Event> { schedule ->
                        Event.ShowSchedule(
                            schedule,
                            if (schedule.weeks.size == 2) dateInteractor.currentWeek else 0
                        )
                    }
                    .onErrorReturn { error ->
                        Timber.e(error)
                        Event.ErrorLoading(error)
                    }
                    .startWith(Event.StartedLoading)
            }
            transformer(Effect.ChangeWeek::class.java) { (week) ->
                just(Event.SwitchWeek(if (week == 1) 0 else 1))
            }
            consumer(Effect.OpenChangeWeekDialog::class.java) { (weeks, selectedWeek) ->
                view.showWeeksDialog(weeks, selectedWeek)
            }
            consumer(Effect.SwitchToCurrentDay::class.java) {
                if (repository.shouldSwitchToDay)
                    view.switchToCurrentDay(dateInteractor.currentDay)
            }
            action(Effect.OpenAddGroup::class.java) {
                navigator.push(Screens.PickInstituteScreen(true))
            }
            action(Effect.OpenSettings::class.java) {
                navigator.push(Screens.SettingsScreen)
            }
        }
    }
}
