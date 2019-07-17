package com.alefimenko.iuttimetable.presentation.di.modules

import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.data.local.schedule.InstitutesDao
import com.alefimenko.iuttimetable.presentation.settings.SettingsRepository
import org.koin.dsl.module.module

/*
 * Created by Alexander Efimenko on 2019-07-17.
 */

val settingsModule = module {
    single {
        SettingsRepository(
            get(),
            get(name = GroupsDao.TAG),
            get(name = InstitutesDao.TAG)
        )
    }
}
