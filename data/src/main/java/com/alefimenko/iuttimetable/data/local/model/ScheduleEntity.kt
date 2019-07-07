package com.alefimenko.iuttimetable.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Parcelize
@Entity(tableName = "schedules")
data class ScheduleEntity @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = -1,
    @ColumnInfo(name = "group_id")
    var groupId: Int = -1,
    @ColumnInfo(name = "schedule")
    var scheduleStr: String = "",
    @ColumnInfo(name = "raw_schedule")
    var rawScheduleStr: String = ""
) : Parcelable
