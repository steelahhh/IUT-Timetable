package com.alefimenko.iuttimetable.schedule.feature

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.common.extension.justOnMain
import com.alefimenko.iuttimetable.data.GroupInfo
import com.alefimenko.iuttimetable.data.date.DateInteractor
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.remote.model.Schedule
import com.alefimenko.iuttimetable.schedule.builder.ScheduleScope
import com.alefimenko.iuttimetable.schedule.data.ScheduleRepository
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.Action
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.Action.Execute
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.Effect
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.News
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.State
import com.alefimenko.iuttimetable.schedule.feature.ScheduleFeature.Wish
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.PostProcessor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject
import javax.inject.Named

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
    bootstrapper = BootStrapperImpl(),
    actor = ActorImpl(
        scheduleRepository,
        dateInteractor,
        preferences,
    ),
    reducer = ReducerImpl(
        repository = scheduleRepository,
    ),
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
        val currentDay: Int = -1,
        @Transient val isLoading: Boolean = false,
        @Transient val isError: Boolean = false,
        val schedule: Schedule? = null
    ) : Parcelable

    sealed class Wish {
        data class DownloadSchedule(val info: GroupInfo) : Wish()
        data class ChangeClassVisibility(val classIndex: Int, val dayIndex: Int, val weekIndex: Int) : Wish()
        data class UpdateCurrentWeek(val week: Int) : Wish()
        data class RestoreAll(val title: String) : Wish()
        data class HideAll(val title: String) : Wish()
        object LoadSchedule : Wish()
        object RequestWeekChange : Wish()
        object RouteToSettings : Wish()
        object RequestDownload : Wish()
        object RouteToGroupPicker : Wish()
    }

    sealed class Action {
        data class Execute(val wish: Wish) : Action()
        object SwitchToCurrentDay : Action()
        object SaveCurrentWeek : Action()
    }

    sealed class Effect {
        object RouteToGroupPicker : Effect()
        object RouteToSettings : Effect()
        object RouteToPickWeek : Effect()
        object StartLoading : Effect()
        data class LoadedWithError(val groupInfo: GroupInfo? = null) : Effect()
        data class ScheduleLoaded(
            val schedule: Schedule,
            val currentWeek: Int
        ) : Effect()

        data class ScheduleUpdated(val schedule: Schedule) : Effect()
        data class ChangeCurrentDay(val day: Int) : Effect()
        data class ChangeCurrentWeek(val week: Int) : Effect()
    }

    sealed class News {
        object OpenGroupPicker : News()
        object OpenSettings : News()
        data class RouteToWeekPicker(val list: List<String>, val selectedWeek: Int) : News()
    }

    class BootStrapperImpl : Bootstrapper<Action> {
        override fun invoke(): Observable<Action> = empty()
    }

    class ActorImpl(
        private val repository: ScheduleRepository,
        private val dateInteractor: DateInteractor,
        private val preferences: Preferences
    ) : Actor<State, Action, Effect> {
        override fun invoke(state: State, action: Action): Observable<Effect> = when (action) {
            is Execute -> handleWish(state, action.wish)
            Action.SwitchToCurrentDay -> if (repository.shouldSwitchToDay)
                justOnMain(Effect.ChangeCurrentDay(dateInteractor.currentDay))
            else
                empty()
            Action.SaveCurrentWeek -> if (
                state.schedule?.weeks?.size == 2 && !repository.shouldSaveWeek
            ) {
                empty()
            } else {
                Single
                    .fromCallable {
                        repository.saveCurrentWeek(state.schedule?.groupTitle, state.selectedWeek)
                    }
                    .flatMapObservable { empty() }
            }
        }

        private fun handleWish(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.DownloadSchedule -> downloadSchedule(wish.info)
            is Wish.RequestWeekChange -> if (state.schedule?.weeks?.size == 2)
                justOnMain<Effect>(Effect.ChangeCurrentWeek(if (state.selectedWeek == 1) 0 else 1))
            else
                justOnMain<Effect>(Effect.RouteToPickWeek)
            is Wish.ChangeClassVisibility -> repository.hideClassAndUpdate(
                wish.classIndex,
                wish.dayIndex,
                wish.weekIndex
            ).ioMainSchedulers().map<Effect> { Effect.ScheduleUpdated(schedule = it) }
            is Wish.UpdateCurrentWeek -> justOnMain(Effect.ChangeCurrentWeek(wish.week))
            Wish.RouteToSettings -> justOnMain(Effect.RouteToSettings)
            Wish.RequestDownload -> state.groupInfo?.let { downloadSchedule(state.groupInfo) } ?: empty()
            Wish.LoadSchedule -> repository.getSchedule(preferences.currentGroup)
                .map<Effect> {
                    Effect.ScheduleLoaded(
                        schedule = it,
                        currentWeek = if (it.weeks.size == 2) dateInteractor.currentWeek else 0
                    )
                }
                .startWith(Effect.StartLoading)
                .onErrorReturnItem(Effect.LoadedWithError())
            Wish.RouteToGroupPicker -> justOnMain(Effect.RouteToGroupPicker)
            is Wish.HideAll -> repository.changeAll(
                title = wish.title,
                hidden = true,
            ).ioMainSchedulers().map<Effect> { Effect.ScheduleUpdated(schedule = it) }
            is Wish.RestoreAll -> repository.changeAll(
                title = wish.title,
                hidden = false,
            ).ioMainSchedulers().map<Effect> { Effect.ScheduleUpdated(schedule = it) }
        }

        private fun downloadSchedule(info: GroupInfo) = repository.downloadSchedule(info)
            .map<Effect> {
                Effect.ScheduleLoaded(
                    schedule = it,
                    currentWeek = if (it.weeks.size == 2) dateInteractor.currentWeek else 0
                )
            }
            .startWith(Effect.StartLoading)
            .onErrorReturnItem(Effect.LoadedWithError(info))
    }

    class ReducerImpl(
        private val repository: ScheduleRepository
    ) : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.StartLoading -> state.copy(
                isLoading = true,
                isError = false
            )
            is Effect.LoadedWithError -> state.copy(
                isLoading = false,
                isError = true,
                groupInfo = effect.groupInfo
            )
            is Effect.ScheduleLoaded -> state.copy(
                isLoading = false,
                isError = false,
                schedule = effect.schedule,
                selectedWeek = if (repository.shouldSaveWeek && effect.schedule.weeks.size != 2)
                    repository.lastSelectedWeek(effect.schedule.groupTitle)
                else
                    effect.currentWeek,
                currentWeek = effect.currentWeek
            )
            is Effect.ChangeCurrentDay -> state.copy(currentDay = effect.day)
            is Effect.ChangeCurrentWeek -> state.copy(selectedWeek = effect.week)
            is Effect.ScheduleUpdated -> state.copy(schedule = effect.schedule)
            else -> state
        }
    }

    class PostProcessorImpl : PostProcessor<Action, Effect, State> {
        override fun invoke(action: Action, effect: Effect, state: State): Action? = when {
            effect is Effect.ScheduleLoaded -> Action.SwitchToCurrentDay
            effect is Effect.ChangeCurrentWeek -> Action.SaveCurrentWeek
            else -> null
        }
    }

    class NewsPublisherImpl : NewsPublisher<Action, Effect, State, News> {
        override fun invoke(action: Action, effect: Effect, state: State): News? = when (effect) {
            is Effect.RouteToPickWeek -> News.RouteToWeekPicker(
                list = state.schedule?.weeks ?: emptyList(),
                selectedWeek = state.selectedWeek
            )
            is Effect.RouteToSettings -> News.OpenSettings
            is Effect.RouteToGroupPicker -> News.OpenGroupPicker
            else -> null
        }
    }

    companion object {
        const val CAPSULE_KEY = "capsule:Schedule"
        const val FEATURE_KEY = "ScheduleFeature"
    }
}
