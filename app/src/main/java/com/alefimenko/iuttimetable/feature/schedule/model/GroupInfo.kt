package com.alefimenko.iuttimetable.feature.schedule.model

import android.os.Parcelable
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2019-03-12.
 */

@Parcelize
data class GroupInfo(
    val form: Int,
    val group: GroupUi,
    val institute: InstituteUi
) : Parcelable
