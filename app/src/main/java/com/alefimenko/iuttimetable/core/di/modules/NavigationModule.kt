package com.alefimenko.iuttimetable.core.di.modules

import com.alefimenko.iuttimetable.core.navigation.Navigator
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2019-03-01.
 */

val navigationModule = module {
    single<Navigator>()
}
