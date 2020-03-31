package com.alefimenko.iuttimetable.presentation.ribs.groups.feature

import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.common.extension.justOnMain
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao.GroupInstitute
import com.alefimenko.iuttimetable.presentation.data.GroupUi
import com.alefimenko.iuttimetable.presentation.data.GroupsRepository
import com.alefimenko.iuttimetable.presentation.ribs.groups.builder.GroupsScope
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature.Action
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature.Action.Execute
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature.Effect
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature.News
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature.State
import com.alefimenko.iuttimetable.presentation.ribs.groups.feature.GroupsFeature.Wish
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.PostProcessor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observable.just
import javax.inject.Inject

@GroupsScope
internal class GroupsFeature @Inject constructor(
    groupsRepository: GroupsRepository
) : BaseFeature<Wish, Action, Effect, State, News>(
    initialState = State(),
    bootstrapper = BootstrapperImpl(),
    wishToAction = { Execute(it) },
    actor = ActorImpl(groupsRepository),
    reducer = ReducerImpl(),
    postProcessor = PostProcessorImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(
        val groups: List<GroupInstitute> = emptyList(),
        val currentGroup: Int = -1
    )

    sealed class Action {
        data class Execute(val wish: Wish) : Action()
        object LoadGroups : Action()
    }

    sealed class Wish {
        object GoBack : Wish()
        object AddNewGroup : Wish()
        data class SelectGroup(val group: GroupUi) : Wish()
        data class DeleteGroup(val group: GroupUi) : Wish()
    }

    sealed class Effect {
        object AddGroup : Effect()
        object GoBack : Effect()
        object GroupDeleted : Effect()
        object LastGroupDeleted : Effect()
        object CurrentGroupUpdated : Effect()
        data class GroupsLoaded(val groups: List<GroupInstitute>, val currentGroup: Int) : Effect()
        data class CurrentGroupDeleted(val groupId: Int) : Effect()
    }

    sealed class News {
        object AddGroup : News()
        object GoBack : News()
        object LastGroupDeleted : News()
        object UpdateNewGroup : News()
    }

    class BootstrapperImpl : Bootstrapper<Action> {
        override fun invoke(): Observable<Action> = justOnMain(Action.LoadGroups)
    }

    class ActorImpl(private val repository: GroupsRepository) : Actor<State, Action, Effect> {
        override fun invoke(state: State, action: Action): Observable<Effect> = when (action) {
            Action.LoadGroups -> repository.getGroups()
                .ioMainSchedulers()
                .toObservable().map {
                    Effect.GroupsLoaded(it, repository.currentGroup)
                }
            is Execute -> handleWish(state, action.wish)
        }

        private fun handleWish(state: State, wish: Wish): Observable<Effect> = when (wish) {
            Wish.GoBack -> justOnMain(Effect.GoBack)
            is Wish.SelectGroup -> {
                Completable.fromAction { repository.currentGroup = wish.group.id }
                    .ioMainSchedulers()
                    .toSingleDefault(true)
                    .toObservable()
                    .map { Effect.CurrentGroupUpdated }
            }
            is Wish.DeleteGroup -> if (wish.group.id == state.currentGroup) {
                repository.deleteCurrentSchedule(wish.group.id)
                    .ioMainSchedulers()
                    .toObservable().map<Effect> { newGroupId ->
                        if (newGroupId != -1) {
                            repository.currentGroup = newGroupId
                            Effect.CurrentGroupDeleted(newGroupId)
                        } else {
                            Effect.LastGroupDeleted
                        }
                    }
            } else {
                repository.deleteSchedule(wish.group.id)
                    .toSingleDefault(true)
                    .ioMainSchedulers()
                    .flatMapObservable<Effect> { just(Effect.GroupDeleted) }
            }
            is Wish.AddNewGroup -> justOnMain(Effect.AddGroup)
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.GroupsLoaded -> state.copy(groups = effect.groups, currentGroup = effect.currentGroup)
            else -> state
        }
    }

    class PostProcessorImpl : PostProcessor<Action, Effect, State> {
        override fun invoke(action: Action, effect: Effect, state: State): Action? = when {
            action is Execute && action.wish is Wish.SelectGroup -> Execute(Wish.GoBack)
            effect is Effect.GroupDeleted -> Action.LoadGroups
            effect is Effect.CurrentGroupDeleted -> Action.LoadGroups
            effect is Effect.CurrentGroupUpdated -> Action.LoadGroups
            else -> null
        }
    }

    class NewsPublisherImpl : NewsPublisher<Action, Effect, State, News> {
        override fun invoke(action: Action, effect: Effect, state: State): News? = when {
            effect is Effect.CurrentGroupDeleted -> News.UpdateNewGroup
            effect is Effect.CurrentGroupUpdated -> News.UpdateNewGroup
            effect is Effect.LastGroupDeleted -> News.LastGroupDeleted
            action is Execute -> handleAction(action)
            else -> null
        }

        private fun handleAction(action: Action): News? = when (action) {
            is Execute -> when (action.wish) {
                is Wish.GoBack -> News.GoBack
                is Wish.AddNewGroup -> News.AddGroup
                else -> null
            }
            else -> null
        }
    }
}
