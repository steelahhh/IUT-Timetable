package com.alefimenko.iuttimetable.presentation.di.modules

import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDao
import com.alefimenko.iuttimetable.presentation.di.Scopes.PICK_GROUP
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupRepository
import org.koin.dsl.module.module

/*
 * Created by Alexander Efimenko on 2019-02-16.
 */

val pickGroupModule = module {
    scope(PICK_GROUP) {
        PickGroupRepository(get(), get(), get(), get(name = SchedulesDao.TAG), get())
    }
}
