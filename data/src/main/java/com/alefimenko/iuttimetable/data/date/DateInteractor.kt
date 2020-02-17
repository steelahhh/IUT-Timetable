package com.alefimenko.iuttimetable.data.date

import com.soywiz.klock.DateTime

/*
 * Created by Alexander Efimenko on 2019-04-21.
 */

interface DateInteractor {
    val today: DateTime
    val isWeekOdd: Boolean
    val currentWeek: Int
    val currentDay: Int
}
