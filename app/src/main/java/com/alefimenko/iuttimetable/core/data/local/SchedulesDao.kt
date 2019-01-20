package com.alefimenko.iuttimetable.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alefimenko.iuttimetable.model.ScheduleRoomModel
import io.reactivex.Maybe
import io.reactivex.Single

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Dao
interface SchedulesDao {

    @get:Query("SELECT * FROM Schedules")
    val schedules: Single<MutableList<ScheduleRoomModel>>

    @Query("SELECT * FROM Schedules WHERE groupid = :groupId")
    fun getScheduleByGroupId(groupId: Int): Maybe<ScheduleRoomModel>

    @Query("DELETE FROM Schedules WHERE groupid = :groupId")
    fun deleteScheduleByGroupId(groupId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: ScheduleRoomModel): Long

    @Query("DELETE FROM Schedules")
    fun deleteAllSchedules(): Int

    @get:Query("SELECT COUNT(*) FROM schedules")
    val groupsCount: Single<Int>

}
