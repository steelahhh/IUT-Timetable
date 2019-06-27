package com.alefimenko.iuttimetable.presentation.schedule

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.BaseEffectHandler
import com.alefimenko.iuttimetable.common.action
import com.alefimenko.iuttimetable.common.effectHandler
import com.alefimenko.iuttimetable.common.transformer
import com.alefimenko.iuttimetable.data.remote.model.Schedule
import com.alefimenko.iuttimetable.navigation.Navigator
import com.alefimenko.iuttimetable.presentation.DateInteractor
import com.alefimenko.iuttimetable.presentation.schedule.model.GroupInfo
import com.spotify.mobius.First
import com.spotify.mobius.First.first
import com.spotify.mobius.Init
import com.spotify.mobius.Next
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
        @Transient val currentDay: Int = 0,
        @Transient val isLoading: Boolean = false,
        @Transient val isError: Boolean = false,
        val schedule: Schedule? = null
    ) : Parcelable

    sealed class Event {
        object StartedLoading : Event()
        data class ShowSchedule(val schedule: Schedule, val currentWeek: Int) : Event()
        object RequestWeekChange : Event()
        data class SwitchWeek(val week: Int) : Event()
        data class ErrorLoading(val throwable: Throwable) : Event()
    }

    sealed class Effect {
        object SwitchToCurrentDay : Effect()
        object DisplaySchedule : Effect()
        data class ChangeWeek(val week: Int) : Effect()
        data class DownloadSchedule(val groupInfo: GroupInfo) : Effect()
        data class OpenChangeWeekDialog(val weeks: List<String>) : Effect()
    }

    object ScheduleInitializer : Init<Model, Effect> {
        override fun init(model: Model): First<Model, Effect> = first(
            model,
            if (model.groupInfo != null)
                setOf(Effect.DownloadSchedule(model.groupInfo))
            else
                setOf(Effect.DisplaySchedule)
        )
    }

    object ScheduleUpdater : Update<Model, Event, Effect> {
        override fun update(model: Model, event: Event): Next<Model, Effect> = when (event) {
            is Event.ShowSchedule -> next(
                model.copy(
                    schedule = event.schedule,
                    isLoading = false,
                    isError = false,
                    currentWeek = event.currentWeek
                ),
                setOf(Effect.SwitchToCurrentDay)
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
            is Event.RequestWeekChange -> next(
                model.copy(isLoading = true),
                if (model.schedule?.weeks?.size == 2)
                    setOf(Effect.ChangeWeek(model.currentWeek))
                else
                    setOf(Effect.OpenChangeWeekDialog(model.schedule?.weeks.orEmpty()))
            )
            is Event.SwitchWeek -> next(
                model.copy(
                    isLoading = false,
                    currentWeek = event.week
                ),
                setOf(Effect.SwitchToCurrentDay)
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
            transformer(Effect.DownloadSchedule::class.java) { effect ->
                repository.downloadSchedule(effect.groupInfo)
                    .map<Event> { schedule ->
                        Event.ShowSchedule(schedule, dateInteractor.currentWeek)
                    }
                    .onErrorReturn { error ->
                        Timber.e(error)
                        Event.ErrorLoading(error)
                    }
                    .startWith(Event.StartedLoading)
            }
            transformer(Effect.DisplaySchedule::class.java) {
                repository.getSchedule()
                    .map<Event> { schedule -> Event.ShowSchedule(schedule, dateInteractor.currentWeek) }
                    .onErrorReturn { error ->
                        Timber.e(error)
                        Event.ErrorLoading(error)
                    }
                    .startWith(Event.StartedLoading)
            }
            transformer(Effect.ChangeWeek::class.java) { effect ->
                just(Event.SwitchWeek(if (effect.week == 1) 0 else 1))
            }
            action(Effect.SwitchToCurrentDay::class.java) {
                view.switchToCurrentDay(dateInteractor.currentDay)
            }
        }
    }
}
