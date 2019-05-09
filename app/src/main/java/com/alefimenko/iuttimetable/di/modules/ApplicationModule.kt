package com.alefimenko.iuttimetable.di.modules

import com.alefimenko.iuttimetable.data.local.di.localDataModule
import com.alefimenko.iuttimetable.feature.pickgroup.di.pickGroupModule
import com.alefimenko.iuttimetable.feature.schedule.di.scheduleModule
import com.alefimenko.iuttimetable.data.remote.di.remoteModule
import com.alefimenko.iuttimetable.navigation.di.navigationModule

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val applicationModule = listOf(
    localDataModule,
    remoteModule,
    dispatcherModule,
    navigationModule,
    preferencesModule
) + listOf(
    pickGroupModule,
    rootModule,
    scheduleModule
)
