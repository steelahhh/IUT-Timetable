package com.alefimenko.iuttimetable.model

import com.google.gson.annotations.SerializedName

/*
 * Created by Alexander Efimenko on 2018-12-11.
 */

data class GroupModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name", alternate = ["label"])
    val name: String
)