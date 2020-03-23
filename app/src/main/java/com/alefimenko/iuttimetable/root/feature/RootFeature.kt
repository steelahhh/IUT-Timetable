package com.alefimenko.iuttimetable.root.feature

import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.common.extension.justOnMain
import com.alefimenko.iuttimetable.data.local.Constants.ITEM_DOESNT_EXIST
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.root.builder.RootScope
import com.alefimenko.iuttimetable.root.feature.RootFeature.Effect
import com.alefimenko.iuttimetable.root.feature.RootFeature.News
import com.alefimenko.iuttimetable.root.feature.RootFeature.State
import com.alefimenko.iuttimetable.root.feature.RootFeature.Wish
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.Observable.just
import javax.inject.Inject
import javax.inject.Named

@RootScope
internal class RootFeature @Inject constructor(
    sharedPreferences: Preferences,
    @Named(GroupsDao.TAG) groupsDao: GroupsDao
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    bootstrapper = BootStrapperImpl(),
    actor = ActorImpl(groupsDao, sharedPreferences),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(val placeholder: Any? = null)

    sealed class Wish {
        object CheckExistingSchedule : Wish()
        object UpdateTheme : Wish()
    }

    sealed class Effect {
        object NoGroup : Effect()
        object ReceivedGroup : Effect()
        data class UpdateTheme(val isNightMode: Boolean) : Effect()
    }

    sealed class News {
        object RouteToSchedule : News()
        object RouteToPickGroup : News()
        data class UpdateTheme(val isNightMode: Boolean) : News()
    }

    class BootStrapperImpl : Bootstrapper<Wish> {
        override fun invoke(): Observable<Wish> = just(Wish.UpdateTheme, Wish.CheckExistingSchedule)
    }

    class ActorImpl(
        private val groupsDao: GroupsDao,
        private val sharedPreferences: Preferences
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.CheckExistingSchedule -> groupsDao.groups
                .ioMainSchedulers()
                .toObservable()
                .map { groups ->
                    val hasNoGroup = groups.isEmpty() || sharedPreferences.currentGroup == ITEM_DOESNT_EXIST
                    if (hasNoGroup) Effect.NoGroup else Effect.ReceivedGroup
                }
            // Push this off to main thread, since MVICore is one needy boi
            is Wish.UpdateTheme -> justOnMain(Effect.UpdateTheme(sharedPreferences.isNightMode))
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = state
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.NoGroup -> News.RouteToPickGroup
            is Effect.ReceivedGroup -> News.RouteToSchedule
            is Effect.UpdateTheme -> News.UpdateTheme(effect.isNightMode)
        }
    }
}
