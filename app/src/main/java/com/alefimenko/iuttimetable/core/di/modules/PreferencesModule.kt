package com.alefimenko.iuttimetable.core.di.modules

import android.content.Context
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.util.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val preferencesModule = module {
    single { LocalPreferences(androidContext().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)) }
}
