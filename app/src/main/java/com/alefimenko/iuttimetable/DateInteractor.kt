package com.alefimenko.iuttimetable

import com.alefimenko.iuttimetable.local.Preferences
import com.soywiz.klock.DateTime
import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.weekOfYear0

/*
 * Created by Alexander Efimenko on 2019-04-21.
 */

interface DateInteractor {
    val today: DateTime
    val isWeekOdd: Boolean
    val currentWeek: Int
    val currentDay: Int
}

class DateInteractorImpl(
    private val localPreferences: Preferences
) : DateInteractor {
    override val today: DateTime get() = DateTime.now()

    override val isWeekOdd: Boolean
        get() {
            var result = today.weekOfYear0 % 2 == 0
            val shouldSwitchWeek = localPreferences.switchWeek

            // switch to the next week if today is sunday
            if (today.dayOfWeek == DayOfWeek.Sunday) result = !result

            // switch the week if switch is enabled in settings
            return if (shouldSwitchWeek) !result else result
        }

    override val currentDay: Int
        get() = when (today.dayOfWeek) {
            // if today is sunday, switch to monday
            DayOfWeek.Sunday -> DayOfWeek.Monday.index0Monday
            else -> today.dayOfWeek.index0Monday
        }

    override val currentWeek: Int
        get() = if (isWeekOdd) 0 else 1
}
