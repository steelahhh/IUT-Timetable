package com.alefimenko.iuttimetable.presentation.di.modules

import com.alefimenko.iuttimetable.presentation.di.Scopes
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupBindings
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupController
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupRepository
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteBindings
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteFeature
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteController
import org.koin.dsl.module.module

/*
 * Created by Alexander Efimenko on 2019-02-16.
 */

val pickGroupModule = module {
    scope(Scopes.PICK_GROUP) {
        PickGroupRepository(get(), get(), get(), get(), get())
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
