package com.alefimenko.iuttimetable.core.data.remote

import com.alefimenko.iuttimetable.core.data.models.GroupEntity
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

/**
 * IUT Timetable
 * Copyright (C) 2018  Alexander Efimenko
 * Email: a.efimenko72@gmail.com
 * Github: github.com/steelahhh
 */

typealias InstituteEntity = GroupEntity

interface ScheduleService {

    /**
     * @param form 0 for ochny form, 1 for zaochny form
     */
    @GET("/{form}/bin/groups.py?act=json_groups")
    fun fetchGroups(
        @Path(value = "form") form: String,
        @Query("id_inst") instituteId: Int
    ): Single<List<GroupEntity>>

    @GET("/wp-content/plugins/tsogu_schedule/schedule.php?selService=yes&type=0")
    fun fetchInstitutes(): Single<List<InstituteEntity>>

    @GET("{form}/bin/groups.py?act=show&print=json")
    fun fetchSchedule(
        @Path(value = "form") form: String,
        @Query("sgroup") groupId: Int
    ): Single<ResponseBody>

    companion object {
        const val ZAOCHNY_FORM = "shedule_new_z"
        const val OCHNY_FORM = "shedule_new"
    }

}