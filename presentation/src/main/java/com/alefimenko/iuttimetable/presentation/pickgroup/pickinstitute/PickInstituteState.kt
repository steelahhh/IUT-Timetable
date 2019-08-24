package com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute

import com.airbnb.mvrx.MvRxState
import com.alefimenko.iuttimetable.presentation.pickgroup.model.InstituteUi

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

data class PickInstituteState(
    val institutes: List<InstituteUi> = listOf(),
    val institute: InstituteUi? = null,
    val form: Int = 0,
    val isLoading: Boolean = false,
    val isError: Boolean = false
) : MvRxState
