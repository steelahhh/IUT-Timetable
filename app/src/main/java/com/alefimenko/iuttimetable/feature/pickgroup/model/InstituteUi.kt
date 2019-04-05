package com.alefimenko.iuttimetable.feature.pickgroup.model

import android.os.Parcelable
import com.alefimenko.iuttimetable.core.data.remote.model.InstituteResponse
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-02-16.
 */

@Parcelize
data class InstituteUi(
    val id: Int,
    val label: String
) : Parcelable {
    companion object {
        fun fromResponse(response: InstituteResponse) = InstituteUi(response.id, response.name)
    }
}
