package com.alefimenko.iuttimetable.data.local.schedule

import android.os.Parcelable
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import com.alefimenko.iuttimetable.data.local.model.GroupEntity
import com.alefimenko.iuttimetable.data.local.model.InstituteEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-06-27.
 */

@Dao
interface GroupsDao {
    @get:Query("SELECT * FROM groups ")
    val groups: Single<List<GroupEntity>>

    @Query("SELECT * FROM groups WHERE group_id = :id")
    fun getById(id: Int): Maybe<GroupEntity>

    @Query("DELETE FROM groups where group_id = :id")
    fun delete(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(group: GroupEntity): Completable

    @Query("select * from groups, institutes where groups.institute_id = institutes.id ")
    fun getGroupsWithInstitute(): Single<List<GroupInstitute>>

    @Parcelize
    data class GroupInstitute(
        @Embedded
        val group: GroupEntity,
        @Relation(
            parentColumn = "institute_id",
            entityColumn = "id",
            entity = InstituteEntity::class
        )
        val institute: List<InstituteEntity>
    ) : Parcelable

    companion object {
        const val TAG = "dao:groups"
    }
}
