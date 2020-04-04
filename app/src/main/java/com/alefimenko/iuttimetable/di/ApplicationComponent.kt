package com.alefimenko.iuttimetable.di

import android.content.Context
import com.alefimenko.iuttimetable.data.DataModule
import com.alefimenko.iuttimetable.root.Root
import javax.inject.Singleton

/*
 * Author: steelahhh
 * 21/3/20
 */

@Singleton
@dagger.Component(modules = [DataModule::class])
interface ApplicationComponent : Root.Dependency {
    @dagger.Component.Factory
    interface Factory {
        fun create(@dagger.BindsInstance applicationContext: Context): ApplicationComponent
    }
}
