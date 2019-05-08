package com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute

import android.os.Parcelable
import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.navigation.Navigator
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupRepository
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.toInstituteUi
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature.Effect
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature.News
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature.State
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature.Wish
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.Observable.just
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-02-17.
 */

class PickInstituteFeature(
    repository: PickGroupRepository,
    navigator: Navigator,
    timeCapsule: AndroidTimeCapsule? = null
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = timeCapsule?.get(PickInstituteFeature::class) ?: State(),
    actor = ActorImpl(
        repository,
        navigator
    ),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    init {
        timeCapsule?.register(PickInstituteFeature::class) {
            state.copy(
                isLoading = false
            )
        }
    }

    data class ViewModel(
        val institutes: List<InstituteUi> = listOf(),
        val institute: InstituteUi? = null,
        val form: Int = 0,
        val isLoading: Boolean,
        val isError: Boolean,
        val isInstitutePicked: Boolean,
        val isInstitutesLoaded: Boolean
    )

    @Parcelize
    data class State(
        val institutes: List<InstituteUi> = listOf(),
        val institute: InstituteUi? = null,
        val form: Int = 0,
        @Transient val isLoading: Boolean = false,
        @Transient val isError: Boolean = false
    ) : Parcelable

    sealed class UiEvent {
        object NextButtonClicked : UiEvent()
        object LoadInstitutesClicked : UiEvent()
        data class InstituteClicked(val institute: InstituteUi) : UiEvent()
        data class FormClicked(val id: Int) : UiEvent()
    }

    sealed class Wish {
        object LoadInstitutes : Wish()
        data class SelectInstitute(val institute: InstituteUi) : Wish()
        data class SelectForm(val id: Int) : Wish()
        object NavigateToPickGroup : Wish()
    }

    sealed class Effect {
        object ScreenChanged : Effect()
        object StartedLoading : Effect()
        data class InstitutesLoaded(val institutes: List<InstituteUi>) : Effect()
        data class InstituteSelected(val institute: InstituteUi) : Effect()
        data class FormSelected(val id: Int) : Effect()
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
            is Wish.LoadInstitutes -> repository.getInstitutes()
                .map<Effect> { Effect.InstitutesLoaded(it) }
                .onErrorReturn { Effect.ErrorLoading(it) }
                .startWith(Effect.StartedLoading)
            is Wish.SelectForm -> just(Effect.FormSelected(wish.id))
            is Wish.SelectInstitute -> just(Effect.InstituteSelected(wish.institute))
            is Wish.NavigateToPickGroup -> Observable.fromCallable {
                navigator.openPickGroup(state.form, state.institute!!)
                return@fromCallable Effect.ScreenChanged
            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.StartedLoading -> state.copy(
                isLoading = true,
                isError = false
            )
            is Effect.InstitutesLoaded -> state.copy(
                institutes = Constants.institutes.map { it.toInstituteUi() },
                isError = false,
                isLoading = false
            )
            is Effect.FormSelected -> state.copy(
                form = effect.id
            )
            is Effect.ErrorLoading -> state.copy(
                isLoading = false,
                isError = true
            )
            is Effect.InstituteSelected -> state.copy(
                institute = effect.institute
            )
            is Effect.ScreenChanged -> state.copy()
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.ErrorLoading -> News.ErrorExecutingRequest(effect.throwable)
            else -> null
        }
    }
}
