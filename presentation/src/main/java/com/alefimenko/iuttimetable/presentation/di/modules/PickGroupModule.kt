package com.alefimenko.iuttimetable.presentation.di.modules

import com.alefimenko.iuttimetable.presentation.di.Scopes.PICK_GROUP
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupBindings
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupController
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupRepository
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.EffectHandler
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.InstituteInitializer
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.InstituteUpdater
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.Model
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.rx2.RxMobius
import org.koin.dsl.module.module
import org.koin.experimental.builder.scope

/*
 * Created by Alexander Efimenko on 2019-02-16.
 */

val pickGroupModule = module {
    scope<PickGroupRepository>(PICK_GROUP)

    single {
        PickGroupFeature(
            repository = get(),
            navigator = get()
        )
    }

    scope(PICK_GROUP) {
        MobiusAndroid.controller(
            RxMobius.loop(
                InstituteUpdater,
                EffectHandler(get(), get()).create()
            ).init(InstituteInitializer).logger(AndroidLogger.tag("PIFFER")),
            Model()
        )
    }

    scope(PICK_GROUP) { (view: PickGroupController) ->
        PickGroupBindings(view, get())
    }
}
