package com.alefimenko.iuttimetable.di.modules

import com.alefimenko.iuttimetable.local.di.localDataModule
import com.alefimenko.iuttimetable.feature.pickgroup.di.pickGroupModule
import com.alefimenko.iuttimetable.feature.schedule.di.scheduleModule
import com.alefimenko.iuttimetable.remote.di.remoteModule

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val applicationModule = listOf(
    localDataModule,
    remoteModule,
    dataModule,
    dispatcherModule,
    navigationModule,
    preferencesModule
) + listOf(
    pickGroupModule,
    rootModule,
    scheduleModule
)
