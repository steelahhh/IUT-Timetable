package com.alefimenko.iuttimetable.di

import android.app.Activity
import com.alefimenko.iuttimetable.IUTApplication

/*
 * Author: steelahhh
 * 22/3/20
 */

interface InjectorProvider {
    val component: ApplicationComponent
}

val Activity.component get() = (this.application as IUTApplication).component
