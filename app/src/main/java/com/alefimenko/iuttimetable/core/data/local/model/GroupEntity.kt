package com.alefimenko.iuttimetable.core.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-03-10.
 */

@Parcelize
@Entity(tableName = "Groups")
data class GroupEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String
) : Parcelable

fun GroupEntity.toUi() = GroupUi(id, name)
