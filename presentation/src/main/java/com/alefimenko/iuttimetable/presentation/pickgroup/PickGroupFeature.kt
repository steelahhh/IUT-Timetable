package com.alefimenko.iuttimetable.presentation.pickgroup

import android.os.Parcelable
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.Effect
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.News
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.State
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.Wish
import com.alefimenko.iuttimetable.presentation.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.presentation.schedule.model.GroupInfo
import com.alefimenko.iuttimetable.navigation.Navigator
import com.alefimenko.iuttimetable.presentation.di.Screens
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.Observable.just
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-03-06.
 */

class PickGroupFeature(
    repository: PickGroupRepository,
    navigator: Navigator,
    timeCapsule: AndroidTimeCapsule? = null
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = timeCapsule?.get(PickGroupFeature::class) ?: State(),
    actor = ActorImpl(
        repository,
        navigator
    ),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class ViewModel(
        val groups: List<GroupUi> = listOf(),
        val group: GroupUi? = null,
        val isLoading: Boolean,
        val isError: Boolean,
        val isGroupPicked: Boolean,
        val isGroupsLoaded: Boolean
    )

    @Parcelize
    data class State(
        val groups: List<GroupUi> = listOf(),
        val group: GroupUi? = null,
        @Transient val isLoading: Boolean = false,
        @Transient val isError: Boolean = false
    ) : Parcelable

    sealed class UiEvent {
        data class LoadGroupsClicked(val form: Int, val instituteId: Int) : UiEvent()
        data class GroupClicked(val groupInfo: GroupInfo) : UiEvent()
    }

    sealed class Wish {
        data class LoadGroups(val form: Int, val instituteId: Int) : Wish()
        data class SelectGroup(val groupInfo: GroupInfo) : Wish()
    }

    sealed class Effect {
        object ScreenChanged : Effect()
        object StartedLoading : Effect()
        data class GroupsLoaded(val groups: List<GroupUi>) : Effect()
        data class GroupSelected(val groupInfo: GroupInfo) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ErrorExecutingRequest(val throwable: Throwable) : News()
    }

    class ActorImpl(
        private val repository: PickGroupRepository,
        private val navigator: Navigator
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.LoadGroups -> repository.getGroups(wish.form, wish.instituteId)
                .map<Effect> { Effect.GroupsLoaded(it) }
                .onErrorReturn { Effect.ErrorLoading(it) }
                .startWith(Effect.StartedLoading)
            is Wish.SelectGroup -> Observable.concat(
                just(Effect.GroupSelected(wish.groupInfo)),
                Observable.fromCallable {
                    navigator.replace(Screens.ScheduleScreen(wish.groupInfo))
                    return@fromCallable Effect.ScreenChanged
                }
            )
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.StartedLoading -> state.copy(
                isLoading = true,
                isError = false
            )
            is Effect.ErrorLoading -> state.copy(
                groups = listOf(),
                isLoading = false,
                isError = true
            )
            is Effect.GroupsLoaded -> state.copy(
                groups = effect.groups,
                isLoading = false,
                isError = false
            )
            is Effect.GroupSelected -> state.copy(
                group = effect.groupInfo.group
            )
            else -> state.copy()
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.ErrorLoading -> News.ErrorExecutingRequest(effect.throwable)
            else -> null
        }
    }
}