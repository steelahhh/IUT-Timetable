package com.alefimenko.iuttimetable.common

import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.KlockLocale

/*
 * Created by Alexander Efimenko on 2019-05-17.
 */

object RussianLocale : KlockLocale() {
    override val ISO639_1: String = "ru"
    override val daysOfWeek: List<String> = listOf(
        "понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье"
    )
    override val firstDayOfWeek: DayOfWeek = DayOfWeek.Monday
    override val months: List<String> = listOf(
        "январь", "февраль", "март", "апрель", "май", "июнь",
        "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"
    )
}
