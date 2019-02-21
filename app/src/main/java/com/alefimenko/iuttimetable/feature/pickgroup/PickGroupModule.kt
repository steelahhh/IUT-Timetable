package com.alefimenko.iuttimetable.feature.pickgroup

import com.alefimenko.iuttimetable.core.di.Scopes
import org.koin.dsl.module.module

/*
 * Created by Alexander Efimenko on 2019-02-16.
 */

val pickGroupModule = module {
    scope(Scopes.PICK_GROUP) {
        PickGroupRepository(get(), get(), get(), get(), get())
    }
}