package com.alefimenko.iuttimetable.core.arch

import androidx.lifecycle.ViewModel
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import javax.inject.Inject

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

open class BasePreferencesViewModel @Inject constructor(
    protected val preferences: LocalPreferences
) : ViewModel() {

    val isNightMode: Boolean
        get() = preferences.isNightMode
}