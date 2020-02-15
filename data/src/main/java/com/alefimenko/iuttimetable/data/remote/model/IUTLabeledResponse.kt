package com.alefimenko.iuttimetable.data.remote.model

import com.google.gson.annotations.SerializedName

/*
 * Created by Alexander Efimenko on 2018-12-11.
 */

data class IUTLabeledResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name", alternate = ["label"])
    val name: String
)
