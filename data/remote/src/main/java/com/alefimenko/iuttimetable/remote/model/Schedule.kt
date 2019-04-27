package com.alefimenko.iuttimetable.remote.model

import android.os.Parcelable
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
    val date: String = "",
    val innerGroup: String = "",
    val hidden: Boolean = false
) : Parcelable, Comparable<ClassEntry> {
    override fun compareTo(other: ClassEntry): Int {
        val equal = 0
        return if (this === other) equal else this.time.start.compareTo(other.time.start)
    }
}

data class Schedule(
    val semester: String = "",
    val weeks: List<String> = mutableListOf(),
    val schedule: Map<Int, WeekSchedule> = mapOf()
)
