package com.alefimenko.iuttimetable.presentation.di.modules

import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.data.local.schedule.InstitutesDao
import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDao
import com.alefimenko.iuttimetable.presentation.CurrentWeekInteractor
import com.alefimenko.iuttimetable.presentation.DateInteractor
import com.alefimenko.iuttimetable.presentation.DateInteractorImpl
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleRepository
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2019-03-13.
 */

val scheduleModule = module {
    single<CurrentWeekInteractor>()
    single {
        ScheduleRepository(
            get(),
            get(),
            get(),
            get(),
            get(name = SchedulesDao.TAG),
            get(name = GroupsDao.TAG),
            get(name = InstitutesDao.TAG),
            get()
        )
    }
    single<DateInteractor> {
        DateInteractorImpl(get())
    }
}
