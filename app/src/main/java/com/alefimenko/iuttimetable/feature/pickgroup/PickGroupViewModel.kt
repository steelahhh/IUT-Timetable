package com.alefimenko.iuttimetable.feature.pickgroup

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.util.Constants
import com.ww.roxie.BaseAction
import com.ww.roxie.BaseState
import com.ww.roxie.BaseViewModel
import com.ww.roxie.Reducer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 2019-02-17.
 */

@Parcelize
data class State(
    val institutes: List<InstituteUi> = listOf(),
    val institute: InstituteUi? = null,
    val form: Int = 0,
    @Transient val isLoading: Boolean = false,
    @Transient val isError: Boolean = false
) : BaseState, Parcelable

sealed class Action : BaseAction {
    object NextButtonClicked : Action()
    object LoadInstitutes : Action()
    data class InstituteClicked(val institute: InstituteUi) : Action()
    data class FormClicked(val id: Int) : Action()
}

sealed class Change {
    object StartedLoading : Change()
    object NextButtonClicked: Change()
    data class InstitutesLoaded(val institutes: List<InstituteUi>) : Change()
    data class InstituteSelected(val institute: InstituteUi) : Change()
    data class FormSelected(val id: Int) : Change()
    data class Error(val throwable: Throwable?) : Change()
}

class PickGroupViewModel(
    initialState: State?,
    val repository: PickGroupRepository
) : BaseViewModel<Action, State>() {
    override val initialState: State = initialState ?: State()

    private fun bindActions() {
        val loadInstitutesChange = actions.ofType<Action.LoadInstitutes>()
            .switchMap {
                repository.getInstitutes()
                    .subscribeOn(Schedulers.io())
                    .map<Change> { institutes ->
                        Change.InstitutesLoaded(institutes.map { institute ->
                            InstituteUi.fromResponse(institute)
                        })
                    }
                    .onErrorReturn { Change.Error(it) }
                    .startWith(Change.StartedLoading)
            }

        val selectInstituteChange = actions.ofType<Action.InstituteClicked>()
            .switchMap { action ->
                Observable.just(Change.InstituteSelected(action.institute))
            }

        val selectFormChange = actions.ofType<Action.FormClicked>()
            .switchMap { action ->
                Observable.just(Change.FormSelected(action.id))
            }

        val nextButtonChange = actions.ofType<Action.NextButtonClicked>()
            .switchMap {
                Observable.just(Change.NextButtonClicked)
            }

        val allChanges = Observable.merge(
            loadInstitutesChange,
            selectInstituteChange,
            selectFormChange,
            nextButtonChange
        )

        disposables += allChanges
            .scan(initialState, reducer)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(state::setValue, Timber::e)
    }

    private val reducer: Reducer<State, Change> = { state, change ->
        when (change) {
            is Change.InstitutesLoaded  -> state.copy(
                isLoading = false,
                isError = false,
                institutes = change.institutes
            )
            Change.StartedLoading       -> state.copy(
                isLoading = true,
                isError = false
            )
            is Change.Error             -> state.copy(
                institutes = Constants.institutesUi,
                isLoading = false,
                isError = true
            )
            is Change.InstituteSelected -> state.copy(
                isLoading = false,
                isError = false,
                institute = change.institute
            )
            is Change.FormSelected      -> state.copy(
                form = change.id
            )
            is Change.NextButtonClicked -> state.copy(
            )
        }
    }

    init {
        bindActions()
    }

}

@Suppress("UNCHECKED_CAST")
class PickGroupViewModelFactory(
    private val initialState: State?,
    private val pickGroupRepository: PickGroupRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PickGroupViewModel(initialState, pickGroupRepository) as T
    }
}
