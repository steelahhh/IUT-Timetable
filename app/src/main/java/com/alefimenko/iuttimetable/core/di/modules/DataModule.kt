package com.alefimenko.iuttimetable.core.di.modules

import androidx.room.Room
import com.alefimenko.iuttimetable.core.data.local.GroupDatabase
import com.alefimenko.iuttimetable.core.data.local.SchedulesDatabase
import com.alefimenko.iuttimetable.model.mappers.ScheduleParser
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

const val schedules = "SchedulesDB"
const val groups = "GroupsDB"

val dataModule = module {
    factory(name = schedules) {
        Room.databaseBuilder(
            androidContext(),
            SchedulesDatabase::class.java, "Schedules"
        ).fallbackToDestructiveMigration().build()
    }

    factory(name = groups) {
        Room.databaseBuilder(
            androidContext(),
            GroupDatabase::class.java, "Groups"
        ).fallbackToDestructiveMigration().build()
    }

    factory { get<SchedulesDatabase>(name = schedules).schedulesDao }

    factory { get<GroupDatabase>(name = groups).groupsDao }

    single<ScheduleParser>()

    single { GsonBuilder().enableComplexMapKeySerialization().create() }
}
