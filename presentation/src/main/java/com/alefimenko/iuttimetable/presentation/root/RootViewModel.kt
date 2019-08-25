package com.alefimenko.iuttimetable.presentation.root

import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.alefimenko.iuttimetable.common.MvRxViewModel
import com.alefimenko.iuttimetable.common.NetworkStatusReceiver
import org.koin.core.KoinComponent
import org.koin.core.get

/*
 * Created by Alexander Efimenko on 2019-08-25.
 */

data class RootState(val placeholder: String = "") : MvRxState

class RootViewModel(
    private val interactor: RootInteractor,
    val networkStatusReceiver: NetworkStatusReceiver,
    initialState: RootState
) : MvRxViewModel<RootState>(initialState) {
    fun updateTheme() = interactor.updateTheme()

    fun setRootScreen() = interactor.setRootScreen()

    override fun onCleared() {
        interactor.onDestroy()
        super.onCleared()
    }

    companion object : MvRxViewModelFactory<RootViewModel, RootState>, KoinComponent {
        override fun create(viewModelContext: ViewModelContext, state: RootState): RootViewModel? {
            return RootViewModel(get(), get(), RootState())
        }
    }
}
