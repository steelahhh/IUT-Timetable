package com.alefimenko.iuttimetable.data.local.schedule

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alefimenko.iuttimetable.data.local.model.ScheduleEntity
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

    @Query("SELECT EXISTS(SELECT * FROM Schedules WHERE group_id = :groupId)")
    fun groupExists(groupId: Int): Single<Boolean>

    @Query("SELECT * FROM Schedules WHERE group_id = :groupId")
    fun getByGroupId(groupId: Int): Maybe<ScheduleEntity>

    @Query("DELETE FROM Schedules WHERE group_id = :id")
    fun deleteByGroupId(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: ScheduleEntity): Completable

    @Query("DELETE FROM Schedules")
    fun deleteAll(): Int

    companion object {
        const val TAG = "dao:schedules"
    }
}
