package com.alefimenko.iuttimetable.core.di.modules

import com.google.gson.Gson
import dagger.Module
import dagger.Provides

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Module
class ApplicationModule {
    @Provides
    fun provideGson(): Gson = Gson()
}