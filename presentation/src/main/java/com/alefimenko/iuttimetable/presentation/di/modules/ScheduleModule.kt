package com.alefimenko.iuttimetable.presentation.di.modules

import com.alefimenko.iuttimetable.data.date.DateInteractor
import com.alefimenko.iuttimetable.data.date.DateInteractorImpl
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.data.local.schedule.InstitutesDao
import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDao
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

/*
 * Created by Alexander Efimenko on 2019-03-13.
 */

val scheduleModule = module {
    single {
        ScheduleRepository(
            get(),
            get(),
            get(),
            get(),
            get(named(SchedulesDao.TAG)),
            get(named(GroupsDao.TAG)),
            get(named(InstitutesDao.TAG)),
            get()
        )
    }
    single<DateInteractor> {
        DateInteractorImpl(get())
    }
}
