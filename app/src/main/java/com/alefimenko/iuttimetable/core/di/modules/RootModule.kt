package com.alefimenko.iuttimetable.core.di.modules

import com.alefimenko.iuttimetable.feature.RootFeature
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2019-04-05.
 */

val rootModule = module {
    single<RootFeature>()
}
