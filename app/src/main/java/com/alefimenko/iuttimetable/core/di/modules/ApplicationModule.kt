package com.alefimenko.iuttimetable.core.di.modules

import com.alefimenko.iuttimetable.feature.pickgroup.di.pickGroupModule
import com.alefimenko.iuttimetable.feature.schedule.di.scheduleModule

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val applicationModule = listOf(
    dataModule,
    dispatcherModule,
    navigationModule,
    networkModule,
    preferencesModule
) + listOf(
    pickGroupModule,
    rootModule,
    scheduleModule
)
