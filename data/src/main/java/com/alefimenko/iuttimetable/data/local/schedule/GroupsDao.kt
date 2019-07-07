package com.alefimenko.iuttimetable.data.local.schedule

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alefimenko.iuttimetable.data.local.model.GroupEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/*
 * Created by Alexander Efimenko on 2019-06-27.
 */

@Dao
interface GroupsDao {
    @get:Query("SELECT * FROM groups ")
    val groups: Single<List<GroupEntity>>

    @Query("SELECT * FROM groups WHERE id = :id")
    fun getById(id: Int): Maybe<GroupEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(group: GroupEntity): Completable

    companion object {
        const val TAG = "dao:groups"
    }
}
