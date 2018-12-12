package com.alefimenko.iuttimetable.core.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Module
class ContextModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context = context
}