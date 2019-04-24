package com.alefimenko.iuttimetable.feature.schedule

import com.alefimenko.iuttimetable.core.data.NetworkStatusReceiver
import com.alefimenko.iuttimetable.core.data.ScheduleParser
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.core.data.local.SchedulesDao
import com.alefimenko.iuttimetable.core.data.local.model.ScheduleEntity
import com.alefimenko.iuttimetable.core.data.remote.FeedbackService
import com.alefimenko.iuttimetable.core.data.remote.ScheduleService
import com.alefimenko.iuttimetable.core.data.remote.model.ScheduleResponse
import com.alefimenko.iuttimetable.core.data.remote.toFormPath
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.feature.schedule.model.GroupInfo
import com.alefimenko.iuttimetable.feature.schedule.model.Schedule
import io.github.steelahhh.common.extension.ioMainSchedulers
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/*
 * Created by Alexander Efimenko on 2019-03-13.
 */

class ScheduleRepository(
    private val preferences: LocalPreferences,
    private val gson: Gson,
    private val scheduleParser: ScheduleParser,
    private val scheduleService: ScheduleService,
    private val feedbackService: FeedbackService,
    private val schedulesDao: SchedulesDao,
    private val networkStatusReceiver: NetworkStatusReceiver
) {

    fun getSchedule() = schedulesDao
        .getByGroupId(preferences.currentGroup)
        .ioMainSchedulers()

    fun downloadSchedule(groupInfo: GroupInfo): Observable<Schedule> {
        val formPath = groupInfo.form.toFormPath()
        return scheduleService.fetchSchedule(formPath, groupInfo.group.id)
            .flatMap { body ->
                createSchedule(body.string())
            }.flatMap { response ->
                saveSchedule(groupInfo, response)
                    .toSingleDefault(response.schedule)
            }
            .toObservable()
            .ioMainSchedulers()
    }

    fun updateSchedule(): Observable<Schedule> {
        return getSchedule().flatMapObservable { schedule ->
            val groupInfo = GroupInfo(
                form = schedule.formId,
                group = GroupUi(
                    schedule.groupId,
                    schedule.groupName
                ),
                institute = InstituteUi(
                    schedule.instituteId,
                    schedule.instituteName
                )
            )
            downloadSchedule(groupInfo)
        }
    }

    private fun saveCurrentGroup(groupInfo: GroupInfo) = Completable.fromAction {
        preferences.currentGroup = groupInfo.group.id
    }

    private fun saveSchedule(
        groupInfo: GroupInfo,
        response: ScheduleResponse
    ) = with(groupInfo) {
        schedulesDao.insert(
            ScheduleEntity(
                formId = form,
                groupId = group.id,
                groupName = group.label,
                instituteId = institute.id,
                instituteName = institute.label,
                scheduleStr = gson.toJson(response.schedule),
                rawScheduleStr = response.rawBody
            )
        ).andThen(saveCurrentGroup(groupInfo))
    }

    private fun createSchedule(body: String): Single<ScheduleResponse> {
        scheduleParser.initialize(body)
        return Single.fromCallable {
            ScheduleResponse(
                rawBody = body,
                schedule = Schedule(
                    semester = scheduleParser.semester,
                    weeks = scheduleParser.weeks,
                    schedule = scheduleParser.schedule
                )
            )
        }
    }
}