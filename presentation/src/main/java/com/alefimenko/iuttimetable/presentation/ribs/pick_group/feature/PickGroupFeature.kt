package com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.common.extension.justOnMain
import com.alefimenko.iuttimetable.data.Group
import com.alefimenko.iuttimetable.data.GroupInfo
import com.alefimenko.iuttimetable.data.Institute
import com.alefimenko.iuttimetable.presentation.model.toGroup
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupRepository
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.builder.PickGroupScope
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature.Action
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature.Action.Execute
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature.Effect
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature.News
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature.State
import com.alefimenko.iuttimetable.presentation.ribs.pick_group.feature.PickGroupFeature.Wish
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

@PickGroupScope
class PickGroupFeature @Inject constructor(
    repository: PickGroupRepository,
    @Named(CAPSULE_KEY)
    private val timeCapsule: AndroidTimeCapsule
) : BaseFeature<Wish, Action, Effect, State, News>(
    initialState = timeCapsule[FEATURE_KEY] ?: State(),
    wishToAction = { Execute(it) },
    bootstrapper = BootStrapperImpl(),
    actor = ActorImpl(repository),
    reducer = ReducerImpl(),
    postProcessor = PostProcessorImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    init {
        timeCapsule.register(FEATURE_KEY) { state }
    }

    @Parcelize
    data class State(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val groups: List<Group> = emptyList(),
        val filteredGroups: List<Group> = emptyList(),
        val form: Int = 0,
        val institute: Institute? = null
    ) : Parcelable

    sealed class Wish {
        object GoBack : Wish()
        object Retry : Wish()
        data class LoadGroups(val form: Int, val institute: Institute) : Wish()
        data class PickGroup(val group: Group) : Wish()
        data class QueryChanged(val query: String) : Wish()
    }

    sealed class Action {
        data class Execute(val wish: Wish) : Action()
        data class SaveGroupInfo(val form: Int, val institute: Institute) : Action()
    }

    sealed class Effect {
        object RouteBack : Effect()
        object Loading : Effect()
        object LoadedWithError : Effect()
        data class GroupsLoaded(val groups: List<Group>) : Effect()
        data class FilterGroups(val query: String) : Effect()
        data class SaveGroupInfo(val form: Int, val institute: Institute) : Effect()
        data class PickGroup(val group: Group, val form: Int, val institute: Institute) : Effect()
    }

    sealed class News {
        object GoBack : News()
        data class RouteToSchedule(val groupInfo: GroupInfo) : News()
    }

    class BootStrapperImpl : Bootstrapper<Action> {
        override fun invoke(): Observable<Action> = empty()
    }

    class ActorImpl(
        private val repository: PickGroupRepository
    ) : Actor<State, Action, Effect> {
        override fun invoke(state: State, action: Action): Observable<Effect> = when (action) {
            is Execute -> handleWish(state, action.wish)
            is Action.SaveGroupInfo -> justOnMain(Effect.SaveGroupInfo(action.form, action.institute))
        }

        private fun handleWish(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.LoadGroups -> repository.getGroups(wish.form, wish.institute.id)
                .map<Effect> { groups -> Effect.GroupsLoaded(groups.map { it.toGroup() }) }
                .onErrorReturnItem(Effect.LoadedWithError)
                .startWith(Effect.Loading)
                .ioMainSchedulers()
            is Wish.GoBack -> justOnMain(Effect.RouteBack)
            is Wish.Retry -> repository.getGroups(state.form, state.institute!!.id)
                .map<Effect> { groups -> Effect.GroupsLoaded(groups.map { it.toGroup() }) }
                .onErrorReturnItem(Effect.LoadedWithError)
                .startWith(Effect.Loading)
                .ioMainSchedulers()
            is Wish.PickGroup -> justOnMain(
                Effect.PickGroup(
                    form = state.form,
                    institute = state.institute!!,
                    group = wish.group
                )
            )
            is Wish.QueryChanged -> justOnMain(Effect.FilterGroups(wish.query))
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.RouteBack -> state
            is Effect.GroupsLoaded -> state.copy(
                isLoading = false,
                isError = false,
                groups = effect.groups,
                filteredGroups = effect.groups
            )
            is Effect.Loading -> state.copy(
                isLoading = true,
                isError = false
            )
            is Effect.LoadedWithError -> state.copy(
                isLoading = false,
                isError = true
            )
            is Effect.FilterGroups -> state.copy(
                filteredGroups = state.groups.filter { it.matchesQuery(effect.query) }
            )
            is Effect.SaveGroupInfo -> state.copy(
                form = effect.form,
                institute = effect.institute
            )
            else -> state
        }

        private fun Group.matchesQuery(query: String): Boolean {
            val label = name.replace(" ", "")
            val digits = query.replace("[a-zA-Zа-яА-Я]+".toRegex(), "").trim()
            val name = query.replace("\\d+".toRegex(), "").trim()
            return label.contains(query, true) || (label.contains(digits) && label.contains(name, true))
        }
    }

    class PostProcessorImpl : PostProcessor<Action, Effect, State> {
        override fun invoke(action: Action, effect: Effect, state: State): Action? = when {
            action is Execute && action.wish is Wish.LoadGroups && state.institute == null -> {
                Action.SaveGroupInfo(action.wish.form, action.wish.institute)
            }
            else -> null
        }
    }

    class NewsPublisherImpl : NewsPublisher<Action, Effect, State, News> {
        override fun invoke(action: Action, effect: Effect, state: State): News? = when (effect) {
            is Effect.RouteBack -> News.GoBack
            is Effect.PickGroup -> News.RouteToSchedule(
                groupInfo = GroupInfo(
                    form = effect.form,
                    institute = effect.institute,
                    group = effect.group
                )
            )
            else -> null
        }
    }

    companion object {
        const val CAPSULE_KEY = "capsule:TimeCapsule"
        const val FEATURE_KEY = "PickGroupFeature"
    }
}
