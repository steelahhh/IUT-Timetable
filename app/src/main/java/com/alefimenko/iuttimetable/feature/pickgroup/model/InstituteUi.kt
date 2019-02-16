package com.alefimenko.iuttimetable.feature.pickgroup.model

import com.alefimenko.iuttimetable.core.data.remote.InstituteResponse

/*
 * Created by Alexander Efimenko on 2019-02-16.
 */

data class InstituteUi(
    val id: Int,
    val label: String
) {
    companion object {
        fun fromResponse(response: InstituteResponse) = InstituteUi(response.id, response.name)
    }
}
