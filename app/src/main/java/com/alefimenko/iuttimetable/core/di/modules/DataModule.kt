package com.alefimenko.iuttimetable.core.di.modules

import androidx.room.Room
import com.alefimenko.iuttimetable.core.data.local.SchedulesDatabase
import com.alefimenko.iuttimetable.model.mappers.ScheduleParser
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val dataModule = module {
    factory {
        Room.databaseBuilder(
            androidContext(),
            SchedulesDatabase::class.java, "Schedules"
        ).fallbackToDestructiveMigration().build()
    }

    factory { get<SchedulesDatabase>().schedulesDao }

    single<ScheduleParser>()

    single { GsonBuilder().enableComplexMapKeySerialization().create() }
}
