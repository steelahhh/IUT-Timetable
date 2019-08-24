package com.alefimenko.iuttimetable.presentation.schedule.groups

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.BaseEffectHandler
import com.alefimenko.iuttimetable.common.consumer
import com.alefimenko.iuttimetable.common.effectHandler
import com.alefimenko.iuttimetable.common.transformer
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.navigation.Navigator
import com.alefimenko.iuttimetable.presentation.di.Screens
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleRepository
import com.spotify.mobius.First
import com.spotify.mobius.First.first
import com.spotify.mobius.Init
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update
import io.reactivex.ObservableTransformer
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-07-30.
 */

object GroupsFeature {
    @Parcelize
    data class GroupsModel(
        @Transient val isLoading: Boolean = false,
        @Transient val isError: Boolean = false,
        val currentGroup: Int = -1,
        val groups: List<GroupsDao.GroupInstitute> = listOf()
    ) : Parcelable

    sealed class Event {
        data class GroupsLoaded(
            val groups: List<GroupsDao.GroupInstitute>,
            val currentGroup: Int
        ) : Event()
        object Loading : Event()
        object NavigateToOpenGroup : Event()
    }

    sealed class Effect {
        object LoadGroups : Effect()
        object OpenAddGroup : Effect()
    }

    object GroupsInitializer : Init<GroupsModel, Effect> {
        override fun init(
            model: GroupsModel
        ): First<GroupsModel, Effect> = first(
            model,
            setOf(Effect.LoadGroups)
        )
    }

    object GroupsUpdater : Update<GroupsModel, Event, Effect> {
        override fun update(model: GroupsModel, event: Event): Next<GroupsModel, Effect> =
            when (event) {
                is Event.GroupsLoaded -> next(
                    model.copy(
                        isLoading = false,
                        groups = event.groups,
                        currentGroup = event.currentGroup,
                        isError = false
                    )
                )
                is Event.Loading -> next(
                    model.copy(
                        isLoading = true,
                        isError = false
                    )
                )
                is Event.NavigateToOpenGroup -> dispatch(setOf(Effect.OpenAddGroup))
            }
    }

    class GroupsEffectHandler(
        private val repository: ScheduleRepository,
        private val navigator: Navigator,
        private val preferences: Preferences,
        private val groupsView: GroupsView
    ) : BaseEffectHandler<Effect, Event>() {
        override fun create(): ObservableTransformer<Effect, Event> = effectHandler {
            transformer(Effect.LoadGroups::class.java) {
                repository.getGroups()
                    .toObservable()
                    .map<Event> {
                        GroupsFeature.Event.GroupsLoaded(it, preferences.currentGroup)
                    }
                    .startWith(Event.Loading)
            }
            consumer(Effect.OpenAddGroup::class.java) {
                navigator.push(Screens.PickInstituteScreenOld(true))
                groupsView.dismissDialog()
            }
        }
    }
}
