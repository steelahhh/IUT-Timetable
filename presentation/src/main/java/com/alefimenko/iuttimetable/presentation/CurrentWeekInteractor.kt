package com.alefimenko.iuttimetable.presentation

import io.reactivex.subjects.BehaviorSubject

/*
 * Created by Alexander Efimenko on 2019-04-22.
 */

class CurrentWeekInteractor(
    private val dateInteractor: DateInteractor
) {
    private val currentWeekSubject = BehaviorSubject.create<Int>()

    init {
        currentWeekSubject.onNext(dateInteractor.currentWeek)
    }

    fun observe() = currentWeekSubject.hide()

    fun accept(week: Int) = currentWeekSubject.onNext(week)

    fun changeWeek() = if (currentWeek == 0) {
        accept(1)
    } else {
        accept(0)
    }

    val currentWeek: Int get() = currentWeekSubject.value ?: dateInteractor.currentWeek
}
