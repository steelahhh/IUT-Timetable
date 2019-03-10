package com.alefimenko.iuttimetable.feature.pickgroup

import com.alefimenko.iuttimetable.core.di.Scopes
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteBindings
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFragment
import org.koin.dsl.module.module
import org.koin.experimental.builder.scope
import org.koin.experimental.builder.single

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

    scope(Scopes.PICK_GROUP) { (view: PickInstituteFragment) ->
        PickInstituteBindings(view, get())
    }

    scope(Scopes.PICK_GROUP) { (view: PickGroupFragment) ->
        PickGroupBindings(view, get())
    }
}
