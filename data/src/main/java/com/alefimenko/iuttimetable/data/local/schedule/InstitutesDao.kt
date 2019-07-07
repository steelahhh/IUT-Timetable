package com.alefimenko.iuttimetable.data.local.schedule

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alefimenko.iuttimetable.data.local.model.InstituteEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/*
 * Created by Alexander Efimenko on 2019-06-27.
 */

@Dao
interface InstitutesDao {
    @get:Query("SELECT * FROM institutes ")
    val institutes: Single<List<InstituteEntity>>

    @Query("SELECT * FROM institutes WHERE id = :id")
    fun getById(id: Int): Maybe<InstituteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(institute: InstituteEntity): Completable

    companion object {
        const val TAG = "dao:institutes"
    }
}
