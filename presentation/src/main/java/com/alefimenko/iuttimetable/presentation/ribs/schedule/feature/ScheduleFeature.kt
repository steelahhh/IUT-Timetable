package com.alefimenko.iuttimetable.presentation.ribs.schedule.feature

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.common.extension.justOnMain
import com.alefimenko.iuttimetable.data.GroupInfo
import com.alefimenko.iuttimetable.data.date.DateInteractor
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.remote.model.Schedule
import com.alefimenko.iuttimetable.presentation.ribs.schedule.builder.ScheduleScope
import com.alefimenko.iuttimetable.presentation.ribs.schedule.feature.ScheduleFeature.Action
import com.alefimenko.iuttimetable.presentation.ribs.schedule.feature.ScheduleFeature.Action.Execute
import com.alefimenko.iuttimetable.presentation.ribs.schedule.feature.ScheduleFeature.Effect
import com.alefimenko.iuttimetable.presentation.ribs.schedule.feature.ScheduleFeature.News
import com.alefimenko.iuttimetable.presentation.ribs.schedule.feature.ScheduleFeature.State
import com.alefimenko.iuttimetable.presentation.ribs.schedule.feature.ScheduleFeature.Wish
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleRepository
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.PostProcessor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import io.reactivex.Observable
import io.reactivex.Observable.empty
import javax.inject.Inject
import javax.inject.Named
import kotlinx.android.parcel.Parcelize

@ScheduleScope
internal class ScheduleFeature @Inject constructor(
    @Named(CAPSULE_KEY)
    private val timeCapsule: AndroidTimeCapsule,
    private val dateInteractor: DateInteractor,
    private val scheduleRepository: ScheduleRepository,
    private val preferences: Preferences
) : BaseFeature<Wish, Action, Effect, State, News>(
    initialState = timeCapsule[FEATURE_KEY] ?: State(),
    wishToAction = { Execute(it) },
    bootstrapper = BootStrapperImpl(
        isLoaded = timeCapsule.get<State>(FEATURE_KEY)?.groupInfo == null
    ),
    actor = ActorImpl(scheduleRepository, dateInteractor, preferences),
    reducer = ReducerImpl(),
    postProcessor = PostProcessorImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    init {
        timeCapsule.register(FEATURE_KEY) { state }
    }

    @Parcelize
    data class State(
        val groupInfo: GroupInfo? = null,
        val currentWeek: Int = -1,
        val selectedWeek: Int = -1,
        val currentDay: Int = 0,
        @Transient val isLoading: Boolean = false,
        @Transient val isError: Boolean = false,
        val schedule: Schedule? = null
    ) : Parcelable

    sealed class Wish {
        data class DownloadSchedule(val info: GroupInfo) : Wish()
        data class ChangeClassVisibility(val classIndex: Int, val dayIndex: Int, val weekIndex: Int) : Wish()
        object RequestWeekChange : Wish()
    }

    sealed class Action {
        data class Execute(val wish: Wish) : Action()
        object LoadSchedule : Action()
        object SwitchToCurrentDay : Action()
    }

    sealed class Effect {
        object StartLoading : Effect()
        object LoadedWithError : Effect()
        data class ScheduleLoaded(
            val schedule: Schedule,
            val currentWeek: Int
        ) : Effect()
        data class ScheduleUpdated(val schedule: Schedule) : Effect()
        data class ChangeCurrentDay(val day: Int) : Effect()
        data class ChangeCurrentWeek(val week: Int) : Effect()
    }

    sealed class News

    class BootStrapperImpl(val isLoaded: Boolean) : Bootstrapper<Action> {
        override fun invoke(): Observable<Action> =
            if (isLoaded) justOnMain(Action.LoadSchedule) else empty()
    }

    class ActorImpl(
        private val repository: ScheduleRepository,
        private val dateInteractor: DateInteractor,
        private val preferences: Preferences
    ) : Actor<State, Action, Effect> {
        override fun invoke(state: State, action: Action): Observable<Effect> = when (action) {
            is Execute -> handleWish(state, action.wish)
            Action.LoadSchedule -> repository.getSchedule(preferences.currentGroup)
                .map<Effect> {
                    Effect.ScheduleLoaded(
                        schedule = it,
                        currentWeek = if (it.weeks.size == 2) dateInteractor.currentWeek else 0
                    )
                }
                .startWith(Effect.StartLoading)
                .onErrorReturnItem(Effect.LoadedWithError)
            Action.SwitchToCurrentDay -> if (repository.shouldSwitchToDay)
                justOnMain<Effect>(Effect.ChangeCurrentDay(dateInteractor.currentDay))
            else
                empty<Effect>()
        }

        private fun handleWish(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.DownloadSchedule -> repository.downloadSchedule(wish.info)
                .map<Effect> {
                    Effect.ScheduleLoaded(
                        schedule = it,
                        currentWeek = if (it.weeks.size == 2) dateInteractor.currentWeek else 0
                    )
                }
                .startWith(Effect.StartLoading)
                .doOnError { it.printStackTrace() }
                .onErrorReturnItem(Effect.LoadedWithError)
            is Wish.RequestWeekChange -> if (state.schedule?.weeks?.size == 2)
                justOnMain<Effect>(Effect.ChangeCurrentWeek(if (state.selectedWeek == 1) 0 else 1))
            else
                TODO()
            is Wish.ChangeClassVisibility -> repository.hideClassAndUpdate(
                wish.classIndex,
                wish.dayIndex,
                wish.weekIndex
            ).ioMainSchedulers().map<Effect> { Effect.ScheduleUpdated(schedule = it) }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.StartLoading -> state.copy(
                isLoading = true,
                isError = false
            )
            is Effect.LoadedWithError -> state.copy(
                isLoading = false,
                isError = true
            )
            is Effect.ScheduleLoaded -> state.copy(
                isLoading = false,
                isError = false,
                schedule = effect.schedule,
                selectedWeek = effect.currentWeek,
                currentWeek = effect.currentWeek
            )
            is Effect.ChangeCurrentDay -> state.copy(currentDay = effect.day)
            is Effect.ChangeCurrentWeek -> state.copy(selectedWeek = effect.week)
            is Effect.ScheduleUpdated -> state.copy(schedule = effect.schedule)
        }
    }

    class PostProcessorImpl : PostProcessor<Action, Effect, State> {
        override fun invoke(action: Action, effect: Effect, state: State): Action? = when {
            effect is Effect.ScheduleLoaded -> Action.SwitchToCurrentDay
            else -> null
        }
    }

    class NewsPublisherImpl : NewsPublisher<Action, Effect, State, News> {
        override fun invoke(action: Action, effect: Effect, state: State): News? = when {
            else -> null
        }
    }

    companion object {
        const val CAPSULE_KEY = "capsule:Schedule"
        const val FEATURE_KEY = "ScheduleFeature"
    }
}
