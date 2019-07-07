package com.alefimenko.iuttimetable.data.local.di

import android.content.Context
import androidx.room.Room
import com.alefimenko.iuttimetable.data.ScheduleParser
import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.data.local.schedule.InstitutesDao
import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDao
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

    single(name = SchedulesDao.TAG) { get<SchedulesDatabase>().schedulesDao }

    single(name = GroupsDao.TAG) { get<SchedulesDatabase>().groupsDao }

    single(name = InstitutesDao.TAG) { get<SchedulesDatabase>().institutesDao }

    single<ScheduleParser>()

    single { GsonBuilder().enableComplexMapKeySerialization().create() }

    single {
        Preferences(
            androidContext().getSharedPreferences(
                Constants.PREFS_NAME, Context.MODE_PRIVATE
            )
        )
    }
}
