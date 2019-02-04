package com.alefimenko.iuttimetable.core.di.modules

import android.content.Context
import androidx.room.Room
import com.alefimenko.iuttimetable.core.data.local.SchedulesDao
import com.alefimenko.iuttimetable.core.data.local.SchedulesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Module
object DataModule {

    @Singleton
    @Provides
    @JvmStatic
    internal fun provideDb(context: Context): SchedulesDatabase {
        return Room.databaseBuilder(
            context,
            SchedulesDatabase::class.java, "Schedules"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    @JvmStatic
    internal fun provideSchedulesDao(database: SchedulesDatabase): SchedulesDao {
        return database.schedulesDao
    }

}