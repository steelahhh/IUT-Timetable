package com.alefimenko.iuttimetable.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alefimenko.iuttimetable.core.data.models.ScheduleRoomModel

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Database(entities = [(ScheduleRoomModel::class)], version = 4, exportSchema = false)
abstract class SchedulesDatabase : RoomDatabase() {
    abstract val schedulesDao: SchedulesDao
}
