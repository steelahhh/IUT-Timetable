package com.alefimenko.iuttimetable.presentation.pickgroup

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.alefimenko.iuttimetable.common.MvRxViewModel
import com.alefimenko.iuttimetable.presentation.di.Scopes
import com.alefimenko.iuttimetable.presentation.pickgroup.model.GroupPreviewUi
import org.koin.core.KoinComponent
import org.koin.core.qualifier.named

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

class PickGroupViewModel(
    private val interactor: PickGroupInteractor,
    initialState: PickGroupState
) : MvRxViewModel<PickGroupState>(initialState) {

    init {
        loadGroups()
    }

    fun loadGroups() = withState { state ->
        interactor.getGroups(state.form, state.institute!!.id)
            .execute {
                if (it is Fail) {
                    it.error.printStackTrace()
                }
                copy(groups = it() ?: listOf(), isError = it is Fail, isLoading = it is Loading)
            }
    }

    fun goToSchedule(group: GroupPreviewUi) = withState {
    }

    fun exit() = interactor.exit()

    fun filterGroups(item: GroupPreviewUi, constraint: CharSequence?): Boolean {
        constraint ?: return false
        val query = constraint.toString().toLowerCase()
        val label = item.label.toLowerCase().replace(" ", "")
        val digits = query.replace("[a-zA-Zа-яА-Я]+".toRegex(), "").trim()
        val name = query.replace("\\d+".toRegex(), "").trim()
        return label.contains(query) || (label.contains(digits) && label.contains(name))
    }

    companion object : MvRxViewModelFactory<PickGroupViewModel, PickGroupState>, KoinComponent {
        override fun initialState(viewModelContext: ViewModelContext): PickGroupState? {
            val args = viewModelContext.args<PickGroupFragment.Args>()
            return PickGroupState(form = args.form, institute = args.institute)
        }

        override fun create(
            viewModelContext: ViewModelContext,
            state: PickGroupState
        ): PickGroupViewModel? {
            return PickGroupViewModel(
                getKoin().getOrCreateScope(
                    Scopes.PICK_GROUP,
                    named(Scopes.PICK_GROUP)
                ).get(),
                state
            )
        }
    }
}
