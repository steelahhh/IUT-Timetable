package com.alefimenko.iuttimetable.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-03-12.
 */

@Parcelize
data class GroupInfo(
    val form: Int,
    val group: Group,
    val institute: Institute
) : Parcelable
