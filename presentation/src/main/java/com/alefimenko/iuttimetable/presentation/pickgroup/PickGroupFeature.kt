package com.alefimenko.iuttimetable.presentation.pickgroup

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.BaseEffectHandler
import com.alefimenko.iuttimetable.common.consumer
import com.alefimenko.iuttimetable.navigation.Navigator
import com.alefimenko.iuttimetable.presentation.di.Screens
import com.alefimenko.iuttimetable.presentation.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.presentation.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.presentation.schedule.model.GroupInfo
import com.spotify.mobius.First
import com.spotify.mobius.First.first
import com.spotify.mobius.Init
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-03-06.
 */

object PickGroupFeature {
    @Parcelize
    data class Model(
        val form: Int = 0,
        val institute: InstituteUi? = null,
        val group: GroupUi? = null,
        val groups: List<GroupUi> = listOf(),
        @Transient val isLoading: Boolean = false,
        @Transient val isError: Boolean = false
    ) : Parcelable

    sealed class Event {
        object StartedLoading : Event()
        object LoadGroups : Event()
        data class GroupSelected(val groupInfo: GroupInfo) : Event()
        data class GroupsLoaded(val groups: List<GroupUi>) : Event()
        data class ErrorLoading(val throwable: Throwable) : Event()
    }

    sealed class Effect {
        data class NavigateToSchedule(val groupInfo: GroupInfo) : Effect()
        data class LoadGroups(val form: Int, val institute: InstituteUi?) : Effect()
    }

    object GroupInitializer : Init<Model, Effect> {
        override fun init(model: Model): First<Model, Effect> =
            first(
                model,
                if (model.groups.isEmpty()) setOf(Effect.LoadGroups(model.form, model.institute)) else setOf()
            )
    }

    object GroupUpdater : Update<Model, Event, Effect> {
        override fun update(model: Model, event: Event): Next<Model, Effect> = when (event) {
            Event.StartedLoading -> next(model.copy(isLoading = true, isError = false))
            Event.LoadGroups -> dispatch(setOf(Effect.LoadGroups(model.form, model.institute)))
            is Event.ErrorLoading -> next(model.copy(isLoading = false, isError = true))
            is Event.GroupSelected -> next(
                model.copy(group = event.groupInfo.group),
                setOf(Effect.NavigateToSchedule(event.groupInfo))
            )
            is Event.GroupsLoaded -> next(
                model.copy(
                    groups = event.groups,
                    isError = false,
                    isLoading = false
                )
            )
        }
    }

    class PickGroupEffectHandler(
        private val repository: PickGroupRepository,
        private val navigator: Navigator
    ) : BaseEffectHandler<Effect, Event>() {
        override fun create(): ObservableTransformer<Effect, Event> {
            return RxMobius.subtypeEffectHandler<Effect, Event>()
                .addTransformer(Effect.LoadGroups::class.java) { effect ->
                    effect.flatMap {
                        repository.getGroups(it.form, it.institute!!.id)
                            .map<Event> { Event.GroupsLoaded(it) }
                            .onErrorReturn { Event.ErrorLoading(it) }
                            .startWith(Event.StartedLoading)
                    }
                }
                .consumer(Effect.NavigateToSchedule::class.java) { effect ->
                    navigator.replace(Screens.ScheduleScreen(effect.groupInfo))
                }
                .build()
        }
    }
}
