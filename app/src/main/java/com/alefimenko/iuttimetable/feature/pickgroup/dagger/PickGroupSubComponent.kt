package com.alefimenko.iuttimetable.feature.pickgroup.dagger

import androidx.lifecycle.ViewModelProvider
import com.alefimenko.iuttimetable.core.arch.EventDispatcher
import com.alefimenko.iuttimetable.core.arch.ViewModelFactory
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.util.concurrent.Executor
import javax.inject.Named

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Subcomponent(modules = [PickGroupSubComponent.PickGroupModule::class])
interface PickGroupSubComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Module
    class PickGroupModule {

        @Provides
        fun provideViewModel(
            preferences: LocalPreferences,
            @Named("dispatcher") mainExecutor: Executor
        ): ViewModelProvider.Factory = ViewModelFactory {
            PickGroupViewModel(preferences, EventDispatcher(mainExecutor))
        }
    }

}