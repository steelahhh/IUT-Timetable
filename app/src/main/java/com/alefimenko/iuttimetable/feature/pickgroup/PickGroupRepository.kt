package com.alefimenko.iuttimetable.feature.pickgroup

import com.alefimenko.iuttimetable.core.data.NetworkStatusReceiver
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.core.data.remote.FeedbackService
import com.alefimenko.iuttimetable.core.data.remote.ScheduleService
import com.alefimenko.iuttimetable.model.mappers.ScheduleParser
import com.alefimenko.iuttimetable.util.ioMainSchedulers

/*
 * Created by Alexander Efimenko on 2019-02-14.
 */

class PickGroupRepository(
    private val preferences: LocalPreferences,
    private val scheduleParser: ScheduleParser,
    private val scheduleService: ScheduleService,
    private val feedbackService: FeedbackService,
    private val networkStatusReceiver: NetworkStatusReceiver
) {

    fun getInstitutes() = scheduleService.fetchInstitutes()
        .toObservable()
        .ioMainSchedulers()

    fun updateTheme() {
        preferences.isNightMode = preferences.isNightMode.not()
    }

}
