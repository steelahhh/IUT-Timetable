package com.alefimenko.iuttimetable.core.di.modules

import com.alefimenko.iuttimetable.feature.pickgroup.pickGroupModule

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val applicationModule = listOf(
    dataModule,
    dispatcherModule,
    networkModule,
    preferencesModule
) + listOf(
    pickGroupModule
)
