package com.alefimenko.iuttimetable.core.data.remote.model

import com.alefimenko.iuttimetable.core.data.local.model.GroupEntity
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.google.gson.annotations.SerializedName

/*
 * Created by Alexander Efimenko on 2018-12-11.
 */

data class GroupResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name", alternate = ["label"])
    val name: String
)

fun GroupResponse.toEntity() = GroupEntity(id, name)

fun GroupResponse.toUi() = GroupUi(id, name)
