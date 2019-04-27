package com.alefimenko.iuttimetable.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-04-24.
 */

@Parcelize
@Entity(tableName = "Groups")
data class GroupEntity(
    @PrimaryKey
    @ColumnInfo(name = "groupId")
    val id: Int,
    @ColumnInfo(name = "groupName")
    val name: String
) : Parcelable
