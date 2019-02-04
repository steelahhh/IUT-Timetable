package com.alefimenko.iuttimetable.core.di.modules

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Module
object ApplicationModule {
    @Provides
    @Singleton
    @JvmStatic
    fun provideGson(): Gson = Gson()
}