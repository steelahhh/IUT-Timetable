package com.alefimenko.iuttimetable.data

import com.alefimenko.iuttimetable.data.local.model.GroupEntity
import com.alefimenko.iuttimetable.data.local.model.InstituteEntity
import com.alefimenko.iuttimetable.data.remote.model.IUTLabeledResponse

/*
 * Created by Alexander Efimenko on 17/2/20.
 */

fun GroupEntity.toDomain() = Group(id, name)
// fun Group.toDb() = GroupEntity(id, name)

fun InstituteEntity.toDomain() = Institute(id, name)
fun Institute.toDb() = InstituteEntity(id, name)

fun IUTLabeledResponse.toInstitute() = Institute(id, name)
fun IUTLabeledResponse.toGroup() = Group(id, name)
