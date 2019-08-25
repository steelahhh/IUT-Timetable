package com.alefimenko.iuttimetable.presentation.di.modules

import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDao
import com.alefimenko.iuttimetable.presentation.di.Scopes.PICK_GROUP
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupInteractor
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupRepository
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteInteractor
import org.koin.core.qualifier.named
import org.koin.dsl.module

/*
 * Created by Alexander Efimenko on 2019-02-16.
 */

val pickGroupModule = module {
    scope(named(PICK_GROUP)) {
        scoped {
            PickGroupRepository(get(), get(), get(), get(named(SchedulesDao.TAG)), get())
        }
        scoped {
            PickInstituteInteractor(get(), get(), get())
        }
        scoped {
            PickGroupInteractor(get(), get())
        }
    }
}
