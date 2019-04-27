package com.alefimenko.iuttimetable.feature.schedule.di

import com.alefimenko.iuttimetable.DateInteractor
import com.alefimenko.iuttimetable.DateInteractorImpl
import com.alefimenko.iuttimetable.feature.schedule.CurrentWeekInteractor
import com.alefimenko.iuttimetable.feature.schedule.ScheduleRepository
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2019-03-13.
 */

val scheduleModule = module {
    single<CurrentWeekInteractor>()
    single<ScheduleRepository>()
    single<DateInteractor> {
        DateInteractorImpl(get())
    }
}
