package com.alefimenko.iuttimetable.feature.pickgroup

import org.koin.dsl.module.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2019-02-16.
 */

val pickGroupModule = module {
    single<PickGroupRepository>()
}