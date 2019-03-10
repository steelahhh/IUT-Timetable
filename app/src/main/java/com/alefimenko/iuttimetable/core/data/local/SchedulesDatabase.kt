package com.alefimenko.iuttimetable.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alefimenko.iuttimetable.core.data.local.model.GroupEntity
import com.alefimenko.iuttimetable.core.data.local.model.ScheduleEntity

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Database(entities = [(ScheduleEntity::class)], version = 4, exportSchema = false)
abstract class SchedulesDatabase : RoomDatabase() {
    abstract val schedulesDao: SchedulesDao
}

@Database(entities = [GroupEntity::class], version = 1, exportSchema = false)
abstract class GroupDatabase : RoomDatabase() {
    abstract val groupsDao: GroupsDao
}
