package com.alefimenko.iuttimetable.presentation.di.modules

import com.alefimenko.iuttimetable.presentation.root.RootFeature
import org.koin.dsl.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2019-04-05.
 */

val rootModule = module {
    single<RootFeature>()
}
