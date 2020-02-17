package com.alefimenko.iuttimetable.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 15/2/20.
 */

@Parcelize
data class Group(val id: Int, val name: String) : Parcelable
