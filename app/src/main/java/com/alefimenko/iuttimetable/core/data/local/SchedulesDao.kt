package com.alefimenko.iuttimetable.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alefimenko.iuttimetable.core.data.local.model.GroupEntity
import com.alefimenko.iuttimetable.core.data.local.model.ScheduleEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Dao
interface SchedulesDao {

    @get:Query("SELECT * FROM Schedules")
    val schedules: Single<MutableList<ScheduleEntity>>

    @Query("SELECT * FROM Schedules WHERE groupid = :groupId")
    fun getScheduleByGroupId(groupId: Int): Maybe<ScheduleEntity>

    @Query("DELETE FROM Schedules WHERE groupid = :groupId")
    fun deleteScheduleByGroupId(groupId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: ScheduleEntity): Long

    @Query("DELETE FROM Schedules")
    fun deleteAllSchedules(): Int
}

@Dao
interface GroupsDao {

    @get:Query("SELECT * FROM Groups")
    val groups: Single<MutableList<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroup(group: GroupEntity): Completable

    @get:Query("SELECT COUNT(*) FROM Groups")
    val groupsCount: Single<Int>
}
