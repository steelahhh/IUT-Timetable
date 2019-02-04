package com.alefimenko.iuttimetable.core.di

import android.content.Context
import com.alefimenko.iuttimetable.core.di.modules.*
import com.alefimenko.iuttimetable.feature.RootActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        DataModule::class,
        DispatcherModule::class,
        NetworkModule::class,
        PreferencesModule::class]
)
interface MainComponent {

    fun inject(rootActivity: RootActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): MainComponent
    }

}