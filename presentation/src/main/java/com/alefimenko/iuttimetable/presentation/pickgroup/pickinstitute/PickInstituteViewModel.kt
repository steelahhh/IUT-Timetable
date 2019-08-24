package com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.alefimenko.iuttimetable.common.MvRxViewModel
import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.presentation.di.Scopes
import com.alefimenko.iuttimetable.presentation.pickgroup.model.InstituteUi
import org.koin.core.KoinComponent
import org.koin.core.qualifier.named

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

class PickInstituteViewModel(
    private val interactor: PickInstituteInteractor,
    initialState: PickInstituteState
) : MvRxViewModel<PickInstituteState>(initialState) {

    init {
        loadInstitutes()
    }

    fun loadInstitutes() {
        setState { copy(isLoading = true, isError = false) }

        interactor.institutes
            .ioMainSchedulers()
            .execute {
                copy(
                    institutes = it() ?: listOf(),
                    isLoading = false,
                    isError = it is Fail
                )
            }
    }

    fun selectInstitute(institute: InstituteUi) = setState { copy(institute = institute) }

    fun selectForm(form: Int) = setState { copy(form = form) }

    fun goToGroups() = withState {
        post {
        }
    }

    fun exit() = interactor.exit()

    companion object : MvRxViewModelFactory<PickInstituteViewModel, PickInstituteState>,
        KoinComponent {
        override fun create(
            viewModelContext: ViewModelContext,
            state: PickInstituteState
        ): PickInstituteViewModel? {
            return PickInstituteViewModel(
                getKoin().getOrCreateScope(
                    Scopes.PICK_GROUP,
                    named(Scopes.PICK_GROUP)
                ).get(),
                PickInstituteState()
            )
        }
    }
}
