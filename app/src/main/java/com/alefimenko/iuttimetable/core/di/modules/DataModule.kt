package com.alefimenko.iuttimetable.core.di.modules

import com.alefimenko.iuttimetable.core.data.ScheduleParser
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val dataModule = module {
    single<ScheduleParser>()
}
