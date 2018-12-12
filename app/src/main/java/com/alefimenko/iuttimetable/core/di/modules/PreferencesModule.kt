package com.alefimenko.iuttimetable.core.di.modules

import android.content.Context
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.util.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Module
class PreferencesModule {
    @Provides
    @Singleton
    fun providePreferences(context: Context): LocalPreferences =
        LocalPreferences(context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE))
}