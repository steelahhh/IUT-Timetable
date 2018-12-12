package com.alefimenko.iuttimetable.core.di

import com.alefimenko.iuttimetable.core.di.modules.*
import com.alefimenko.iuttimetable.feature.pickgroup.dagger.PickGroupSubComponent
import dagger.Component
import javax.inject.Singleton

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Component(
    modules = [
        ContextModule::class,
        ApplicationModule::class,
        DataModule::class,
        DispatcherModule::class,
        NetworkModule::class,
        PreferencesModule::class
    ]
)
@Singleton
interface MainComponent {

    fun pickGroupSubComponent(): PickGroupSubComponent

    companion object {
        lateinit var instance: MainComponent
    }
}