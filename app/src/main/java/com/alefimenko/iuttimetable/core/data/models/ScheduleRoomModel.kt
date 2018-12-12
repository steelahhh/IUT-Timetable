package com.alefimenko.iuttimetable.core.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

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

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(formId)
        parcel.writeInt(groupId)
        parcel.writeString(groupName)
        parcel.writeInt(instituteId)
        parcel.writeString(instituteName)
        parcel.writeString(scheduleStr)
        parcel.writeString(rawScheduleStr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScheduleRoomModel> {
        override fun createFromParcel(parcel: Parcel): ScheduleRoomModel {
            return ScheduleRoomModel(parcel)
        }

        override fun newArray(size: Int): Array<ScheduleRoomModel?> {
            return arrayOfNulls(size)
        }
    }
}