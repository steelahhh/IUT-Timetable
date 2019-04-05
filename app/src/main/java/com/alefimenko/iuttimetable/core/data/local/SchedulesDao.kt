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

    @get:Query("SELECT groupId, groupName FROM SCHEDULES ")
    val groups: Single<List<GroupEntity>>

    @Query("SELECT * FROM Schedules WHERE groupId = :groupId")
    fun getByGroupId(groupId: Int): Maybe<ScheduleEntity>

    @Query("DELETE FROM Schedules WHERE groupId = :id")
    fun deleteByGroupId(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: ScheduleEntity): Completable

    @Query("DELETE FROM Schedules")
    fun deleteAll(): Int
}
