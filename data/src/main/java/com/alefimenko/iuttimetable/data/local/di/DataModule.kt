package com.alefimenko.iuttimetable.data.local.di

import androidx.room.Room
import com.alefimenko.iuttimetable.data.ScheduleParser
import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDatabase
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2019-04-24.
 */

val localDataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            SchedulesDatabase::class.java, "Schedules"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<SchedulesDatabase>().schedulesDao }

    single<ScheduleParser>()

    single { GsonBuilder().enableComplexMapKeySerialization().create() }
}
