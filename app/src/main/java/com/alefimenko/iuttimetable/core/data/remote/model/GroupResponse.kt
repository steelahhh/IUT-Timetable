package com.alefimenko.iuttimetable.core.data.remote.model

import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.model.GroupEntity
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

typealias InstituteResponse = GroupResponse

fun GroupResponse.toEntity() = GroupEntity(id, name)

fun GroupResponse.toGroupUi() = GroupUi(id, name)
fun GroupResponse.toInstituteUi() = InstituteUi(id, name)
