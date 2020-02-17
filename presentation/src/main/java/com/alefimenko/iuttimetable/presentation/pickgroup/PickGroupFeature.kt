package com.alefimenko.iuttimetable.presentation.pickgroup

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.BaseEffectHandler
import com.alefimenko.iuttimetable.common.consumer
import com.alefimenko.iuttimetable.common.effectHandler
import com.alefimenko.iuttimetable.common.transformer
import com.alefimenko.iuttimetable.data.Group
import com.alefimenko.iuttimetable.data.GroupInfo
import com.alefimenko.iuttimetable.data.Institute
import com.alefimenko.iuttimetable.navigation.Navigator
import com.alefimenko.iuttimetable.presentation.Screens
import com.spotify.mobius.First
import com.spotify.mobius.First.first
import com.spotify.mobius.Init
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-03-06.
 */

object PickGroupFeature {
    @Parcelize
    data class Model(
        val form: Int = 0,
        val institute: Institute? = null,
        val group: Group? = null,
        val groups: List<GroupItem> = listOf(),
        val filteredGroups: List<GroupItem> = listOf(),
        @Transient val isLoading: Boolean = false,
        @Transient val isError: Boolean = false
    ) : Parcelable

    sealed class Event {
        object StartedLoading : Event()
        object LoadGroups : Event()
        data class QueryChanged(val query: String) : Event()
        data class GroupSelected(val group: Group) : Event()
        data class GroupsLoaded(val groups: List<GroupItem>) : Event()
        data class ErrorLoading(val throwable: Throwable) : Event()
    }

    sealed class Effect {
        data class NavigateToSchedule(val groupInfo: GroupInfo) : Effect()
        data class LoadGroups(val form: Int, val institute: Institute?) : Effect()
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
                model.copy(group = event.group),
                setOf(
                    Effect.NavigateToSchedule(
                        GroupInfo(
                            model.form,
                            event.group,
                            model.institute!!
                        )
                    )
                )
            )
            is Event.GroupsLoaded -> next(
                model.copy(
                    groups = event.groups,
                    filteredGroups = event.groups,
                    isError = false,
                    isLoading = false
                )
            )
            is Event.QueryChanged -> next(
                model.copy(
                    filteredGroups = when {
                        event.query.isEmpty() -> model.groups
                        else -> model.groups.filter { filterGroups(groupName = it.label, query = event.query) }
                    }
                )
            )
        }
    }

    class PickGroupEffectHandler(
        private val repository: PickGroupRepository,
        private val navigator: Navigator
    ) : BaseEffectHandler<Effect, Event>() {
        override fun create(): ObservableTransformer<Effect, Event> = effectHandler {
            transformer(Effect.LoadGroups::class.java) { effect ->
                if (effect.institute == null) Observable.empty<Event>()
                else repository.getGroups(effect.form, effect.institute.id)
                    .map<Event> { Event.GroupsLoaded(it.map { group ->
                        GroupItem(
                            group.id,
                            group.name
                        )
                    }) }
                    .onErrorReturn { Event.ErrorLoading(it) }
                    .startWith(Event.StartedLoading)
            }
            consumer(Effect.NavigateToSchedule::class.java) { effect ->
                navigator.replace(Screens.ScheduleScreen(effect.groupInfo))
            }
        }
    }

    private fun filterGroups(groupName: String, query: String): Boolean {
        val label = groupName.replace(" ", "")
        val digits = query.replace("[a-zA-Zа-яА-Я]+".toRegex(), "").trim()
        val name = query.replace("\\d+".toRegex(), "").trim()
        return label.contains(query, true) || (label.contains(digits) && label.contains(name, true))
    }
}
