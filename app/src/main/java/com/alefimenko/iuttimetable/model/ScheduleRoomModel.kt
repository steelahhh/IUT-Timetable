package com.alefimenko.iuttimetable.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Parcelize
@Entity(tableName = "schedules")
data class ScheduleRoomModel @JvmOverloads constructor(
    @ColumnInfo(name = "formid")
    var formId: Int = 0,
    @PrimaryKey @ColumnInfo(name = "groupid") var groupId: Int = -1,
    @ColumnInfo(name = "groupname")
    var groupName: String = "",
    @ColumnInfo(name = "instituteid")
    var instituteId: Int = -1,
    @ColumnInfo(name = "institutename")
    var instituteName: String = "",
    @ColumnInfo(name = "schedule")
    var scheduleStr: String = "",
    @ColumnInfo(name = "rawSchedule")
    var rawScheduleStr: String = ""
) : Parcelable {

    val schedule: ScheduleEntity
        get() = Gson().fromJson(scheduleStr, ScheduleEntity::class.java)

    val rawSchedule: ScheduleEntity
        get() = Gson().fromJson(rawScheduleStr, ScheduleEntity::class.java)

}
