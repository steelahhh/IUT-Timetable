package com.alefimenko.iuttimetable.core.di

import android.app.Activity

/*
 * Created by Alexander Efimenko on 2019-02-03.
 */

interface DaggerComponentProvider {
    val component: MainComponent
}

val Activity.injector get() = (application as DaggerComponentProvider).component
