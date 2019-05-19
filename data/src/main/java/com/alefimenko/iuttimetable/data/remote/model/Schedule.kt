package com.alefimenko.iuttimetable.data.remote.model

import android.os.Parcelable
import com.alefimenko.iuttimetable.data.local.Constants.EMPTY_ENTRY
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

typealias WeekSchedule = MutableList<List<ClassEntry>>

@Parcelize
data class Time(
    val start: String = "",
    val finish: String = ""
) : Parcelable

@Parcelize
data class ClassEntry(
    val subject: String = "",
    val teacher: String = "",
    val classType: String = "",
    val time: Time = Time(),
    val location: String = "",
    val date: String = EMPTY_ENTRY,
    val innerGroup: String = EMPTY_ENTRY,
    val hidden: Boolean = false
) : Parcelable, Comparable<ClassEntry> {
    override fun compareTo(other: ClassEntry): Int {
        val equal = 0
        return if (this === other) equal else this.time.start.compareTo(other.time.start)
    }
}

val ClassEntry.hasInnerGroup: Boolean
    get() = innerGroup != EMPTY_ENTRY

val ClassEntry.hasDate: Boolean
    get() = date != EMPTY_ENTRY

@Parcelize
data class Schedule(
    val semester: String = "",
    val weeks: List<String> = mutableListOf(),
    val weekSchedule: Map<Int, WeekSchedule> = mapOf()
) : Parcelable
