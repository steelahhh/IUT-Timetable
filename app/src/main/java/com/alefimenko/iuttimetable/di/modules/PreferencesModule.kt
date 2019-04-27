package com.alefimenko.iuttimetable.di.modules

import android.content.Context
import com.alefimenko.iuttimetable.local.Constants
import com.alefimenko.iuttimetable.local.Preferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val preferencesModule = module {
    single {
        Preferences(
            androidContext().getSharedPreferences(
                Constants.PREFS_NAME, Context.MODE_PRIVATE
            )
        )
    }
}
