package com.alefimenko.iuttimetable.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-04-24.
 */

@Parcelize
@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey
    @ColumnInfo(name = "group_id")
    val id: Int,
    @ColumnInfo(name = "group_name")
    val name: String,
    @ColumnInfo(name = "form_id")
    val form: Int,
    @ColumnInfo(name = "institute_id")
    val instituteId: Int,
    @ColumnInfo(name = "institute_semester")
    val semester: String
) : Parcelable
