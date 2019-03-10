package com.alefimenko.iuttimetable.feature.pickgroup.di

import com.alefimenko.iuttimetable.core.di.Scopes
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupBindings
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupFeature
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupController
import com.alefimenko.iuttimetable.feature.pickgroup.PickGroupRepository
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteBindings
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteController
import org.koin.dsl.module.module

/*
 * Created by Alexander Efimenko on 2019-02-16.
 */

val pickGroupModule = module {
    scope(Scopes.PICK_GROUP) {
        PickGroupRepository(get(), get(), get(), get(), get(), get())
    }

    single {
        PickInstituteFeature(
            repository = get(),
            navigator = get()
        )
    }

    single {
        PickGroupFeature(
            repository = get(),
            navigator = get()
        )
    }

    scope(Scopes.PICK_GROUP) { (view: PickInstituteController) ->
        PickInstituteBindings(view, get())
    }

    scope(Scopes.PICK_GROUP) { (view: PickGroupController) ->
        PickGroupBindings(view, get())
    }
}
