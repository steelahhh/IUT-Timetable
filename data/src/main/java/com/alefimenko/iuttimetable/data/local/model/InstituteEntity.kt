package com.alefimenko.iuttimetable.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-06-27.
 */

@Parcelize
@Entity(tableName = "institutes")
data class InstituteEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String
) : Parcelable
