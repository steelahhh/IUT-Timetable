package com.alefimenko.iuttimetable.presentation.di.modules

import com.alefimenko.iuttimetable.presentation.di.Scopes.PICK_GROUP
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupRepository
import org.koin.dsl.module.module
import org.koin.experimental.builder.scope

/*
 * Created by Alexander Efimenko on 2019-02-16.
 */

val pickGroupModule = module {
    scope<PickGroupRepository>(PICK_GROUP)
}
