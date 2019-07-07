package com.alefimenko.iuttimetable.presentation.schedule.model

import com.alefimenko.iuttimetable.data.remote.model.GroupResponse
import com.alefimenko.iuttimetable.presentation.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.presentation.pickgroup.model.InstituteUi

/*
 * Created by Alexander Efimenko on 2019-04-24.
 */

fun GroupResponse.toGroupUi() = GroupUi(id, name)
fun GroupResponse.toInstituteUi() = InstituteUi(id, name)
