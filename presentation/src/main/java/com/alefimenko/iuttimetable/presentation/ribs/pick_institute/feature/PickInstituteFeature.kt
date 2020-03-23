package com.alefimenko.iuttimetable.presentation.ribs.pick_institute.feature

import android.os.Parcelable
import com.alefimenko.iuttimetable.common.EmptyObserver
import com.alefimenko.iuttimetable.common.extension.justOnMain
import com.alefimenko.iuttimetable.data.Institute
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupRepository
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.feature.PickInstituteFeature.Effect
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.feature.PickInstituteFeature.News
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.feature.PickInstituteFeature.State
import com.alefimenko.iuttimetable.presentation.ribs.pick_institute.feature.PickInstituteFeature.Wish
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.just
import javax.inject.Inject
import javax.inject.Named
import kotlinx.android.parcel.Parcelize

internal class PickInstituteFeature @Inject constructor(
    pickGroupRepository: PickGroupRepository,
    @Named(CAPSULE_KEY)
    private val androidTimeCapsule: AndroidTimeCapsule
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = androidTimeCapsule[FEATURE_KEY] ?: State(),
    bootstrapper = BootStrapperImpl(
        institutesLoaded = androidTimeCapsule.get<State>(FEATURE_KEY)?.institutes?.isNotEmpty() ?: false
    ),
    actor = ActorImpl(pickGroupRepository),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    init {
        subscribe(object : EmptyObserver<State>() {
            override fun onNext(value: State) = androidTimeCapsule.register(FEATURE_KEY) {
                value.copy(
                    isLoading = false,
                    isError = false
                )
            }
        })
    }

    @Parcelize
    data class State(
        val institute: Institute? = null,
        val institutes: List<Institute> = emptyList(),
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val selectedForm: Int = 0
    ) : Parcelable

    sealed class Wish {
        object FetchInstitutes : Wish()
        data class FormPicked(val form: Int) : Wish()
        data class InstitutePicked(val institute: Institute) : Wish()
    }

    sealed class Effect {
        object StartLoading : Effect()
        object LoadedWithError : Effect()
        data class InstitutesLoaded(val institutes: List<Institute>) : Effect()
        data class SelectForm(val form: Int) : Effect()
        data class InstitutePicked(val institute: Institute) : Effect()
    }

    sealed class News

    class BootStrapperImpl(private val institutesLoaded: Boolean) : Bootstrapper<Wish> {
        override fun invoke(): Observable<Wish> = if (institutesLoaded) empty() else just(Wish.FetchInstitutes)
    }

    class ActorImpl(
        private val pickGroupRepository: PickGroupRepository
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.FetchInstitutes -> pickGroupRepository.getInstitutes()
                .map<Effect> { Effect.InstitutesLoaded(it) }
                .onErrorReturnItem(Effect.LoadedWithError)
                .startWith(Effect.StartLoading)
            is Wish.FormPicked -> justOnMain(Effect.SelectForm(wish.form))
            is Wish.InstitutePicked -> justOnMain(Effect.InstitutePicked(wish.institute))
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            Effect.StartLoading -> state.copy(isLoading = true, isError = false)
            Effect.LoadedWithError -> state.copy(isLoading = false, isError = true)
            is Effect.InstitutesLoaded -> state.copy(
                isError = false,
                isLoading = false,
                institutes = effect.institutes
            )
            is Effect.SelectForm -> state.copy(selectedForm = effect.form)
            is Effect.InstitutePicked -> state.copy(institute = effect.institute)
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = null
    }

    companion object {
        const val CAPSULE_KEY = "capsule:TimeCapsule"
        const val FEATURE_KEY = "PickInstituteFeature"
    }
}
