package com.alefimenko.iuttimetable.core.di.modules

import com.google.gson.Gson
import dagger.Module
import javax.inject.Singleton

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Module
class ApplicationModule {
    @Singleton
    fun provideGson(): Gson = Gson()
}