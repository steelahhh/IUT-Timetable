package com.alefimenko.iuttimetable.presentation.pickinstitute

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.BaseEffectHandler
import com.alefimenko.iuttimetable.common.action
import com.alefimenko.iuttimetable.common.consumer
import com.alefimenko.iuttimetable.common.effectHandler
import com.alefimenko.iuttimetable.common.transformer
import com.alefimenko.iuttimetable.data.Institute
import com.alefimenko.iuttimetable.navigation.Navigator
import com.alefimenko.iuttimetable.presentation.Screens
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupRepository
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
 * Created by Alexander Efimenko on 2019-02-17.
 */

object PickInstituteFeature {

    @Parcelize
    data class Model(
        val institutes: List<Institute> = listOf(),
        val institute: Institute? = null,
        val form: Int = 0,
        @Transient val isLoading: Boolean = false,
        @Transient val isError: Boolean = false
    ) : Parcelable

    sealed class Event {
        object BackClicked : Event()
        object NextButtonClicked : Event()
        object StartedLoading : Event()
        object LoadInstitutes : Event()
        data class InstitutesLoaded(val institutes: List<Institute>) : Event()
        data class ErrorLoading(val throwable: Throwable) : Event()
        data class InstituteClicked(val institute: Institute) : Event()
        data class FormClicked(val id: Int) : Event()
    }

    sealed class Effect {
        object LoadInstitutes : Effect()
        object NavigateBack : Effect()
        data class NavigateToPickGroup(val form: Int, val institute: Institute) : Effect()
    }

    object InstituteInitializer : Init<Model, Effect> {
        override fun init(model: Model): First<Model, Effect> =
            first(model, if (model.institutes.isEmpty()) setOf(Effect.LoadInstitutes) else setOf())
    }

    object InstituteUpdater : Update<Model, Event, Effect> {
        override fun update(model: Model, event: Event): Next<Model, Effect> = when (event) {
            is Event.ErrorLoading -> next(model.copy(isLoading = false, isError = true))
            is Event.InstituteClicked -> next(model.copy(institute = event.institute))
            is Event.FormClicked -> next(model.copy(form = event.id))
            Event.LoadInstitutes -> dispatch(setOf(Effect.LoadInstitutes))
            Event.StartedLoading -> next(model.copy(isLoading = true, isError = false))
            Event.NextButtonClicked -> dispatch(setOf(Effect.NavigateToPickGroup(model.form, model.institute!!)))
            Event.BackClicked -> dispatch(setOf(Effect.NavigateBack))
            is Event.InstitutesLoaded -> next(
                model.copy(
                    institutes = event.institutes,
                    isError = false,
                    isLoading = false
                )
            )
        }
    }

    class InstituteEffectHandler(
        private val repository: PickGroupRepository,
        private val navigator: Navigator
    ) : BaseEffectHandler<Effect, Event>() {
        override fun create(): ObservableTransformer<Effect, Event> {
            return effectHandler {
                transformer(Effect.LoadInstitutes::class.java) {
                    repository.getInstitutes()
                        .map<Event> { Event.InstitutesLoaded(it) }
                        .onErrorReturn { Event.ErrorLoading(it) }
                        .startWith(Event.StartedLoading)
                }
                consumer(Effect.NavigateToPickGroup::class.java) { effect ->
                    navigator.push(Screens.PickGroupScreen(effect.form, effect.institute))
                }
                action(Effect.NavigateBack::class.java) {
                    navigator.exit()
                }
            }
        }
    }
}
