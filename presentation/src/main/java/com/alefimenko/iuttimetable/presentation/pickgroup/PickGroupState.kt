package com.alefimenko.iuttimetable.presentation.pickgroup

import com.airbnb.mvrx.MvRxState
import com.alefimenko.iuttimetable.presentation.pickgroup.model.GroupPreviewUi
import com.alefimenko.iuttimetable.presentation.pickgroup.model.InstituteUi

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

data class PickGroupState(
    val form: Int = 0,
    val institute: InstituteUi? = null,
    val group: GroupPreviewUi? = null,
    val groups: List<GroupPreviewUi> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
) : MvRxState
