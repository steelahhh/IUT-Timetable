package com.alefimenko.iuttimetable.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 17/2/20.
 */

@Parcelize
data class Institute(val id: Int, val name: String) : Parcelable
