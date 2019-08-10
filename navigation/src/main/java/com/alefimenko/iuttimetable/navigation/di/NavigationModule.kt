package com.alefimenko.iuttimetable.navigation.di

import com.alefimenko.iuttimetable.navigation.Navigator
import org.koin.dsl.module
import org.koin.experimental.builder.single

/*
 * Created by Alexander Efimenko on 2019-03-01.
 */

val navigationModule = module {
    single<Navigator>()
}
