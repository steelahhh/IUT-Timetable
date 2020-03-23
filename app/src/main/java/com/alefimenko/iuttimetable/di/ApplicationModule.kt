package com.alefimenko.iuttimetable.di

import com.alefimenko.iuttimetable.data.local.di.localDataModule
import com.alefimenko.iuttimetable.data.remote.di.remoteModule
import com.alefimenko.iuttimetable.navigation.di.navigationModule
import com.alefimenko.iuttimetable.presentation.di.modules.pickGroupModule
import com.alefimenko.iuttimetable.presentation.di.modules.scheduleModule
import com.alefimenko.iuttimetable.presentation.di.modules.settingsModule

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val applicationModule = listOf(
    localDataModule,
    remoteModule,
    dispatcherModule,
    navigationModule
) + listOf(
    pickGroupModule,
    scheduleModule,
    settingsModule
)
