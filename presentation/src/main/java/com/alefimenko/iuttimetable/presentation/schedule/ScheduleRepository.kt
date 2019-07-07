package com.alefimenko.iuttimetable.presentation.schedule

import com.alefimenko.iuttimetable.common.NetworkStatusReceiver
import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.data.ScheduleParser
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.local.model.GroupEntity
import com.alefimenko.iuttimetable.data.local.model.InstituteEntity
import com.alefimenko.iuttimetable.data.local.model.ScheduleEntity
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.data.local.schedule.InstitutesDao
import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDao
import com.alefimenko.iuttimetable.data.remote.FeedbackService
import com.alefimenko.iuttimetable.data.remote.ScheduleService
import com.alefimenko.iuttimetable.data.remote.model.Schedule
import com.alefimenko.iuttimetable.data.remote.model.ScheduleResponse
import com.alefimenko.iuttimetable.data.remote.toFormPath
import com.alefimenko.iuttimetable.presentation.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.presentation.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.presentation.schedule.model.GroupInfo
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith

/*
 * Created by Alexander Efimenko on 2019-03-13.
 */

class ScheduleRepository(
    private val preferences: Preferences,
    private val gson: Gson,
    private val scheduleParser: ScheduleParser,
    private val scheduleService: ScheduleService,
    private val feedbackService: FeedbackService,
    private val schedulesDao: SchedulesDao,
    private val groupsDao: GroupsDao,
    private val instituteDao: InstitutesDao,
    private val networkStatusReceiver: NetworkStatusReceiver
) {

    fun getSchedule(): Observable<Schedule> = schedulesDao
        .getByGroupId(preferences.currentGroup)
        .map { gson.fromJson(it.scheduleStr, Schedule::class.java) }
        .toObservable()
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
        return groupsDao.getById(preferences.currentGroup).flatMap { group ->
            Maybe.fromCallable { group }.zipWith(instituteDao.getById(group.instituteId))
        }
            .flatMapObservable { (group, institute) ->
                val groupInfo = GroupInfo(
                    form = group.form,
                    group = GroupUi(
                        group.id,
                        group.name
                    ),
                    institute = InstituteUi(
                        institute.id,
                        institute.name
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
                groupId = group.id,
                scheduleStr = gson.toJson(response.schedule),
                rawScheduleStr = response.rawBody
            )
        ).andThen(
            groupsDao.insert(
                GroupEntity(
                    group.id,
                    group.label,
                    form,
                    institute.id,
                    response.schedule.semester
                )
            )
        ).andThen(
            instituteDao.insert(
                InstituteEntity(
                    institute.id,
                    institute.label
                )
            )
        ).andThen(
            saveCurrentGroup(groupInfo)
        )
    }

    private fun createSchedule(body: String): Single<ScheduleResponse> {
        return Single.defer {
            scheduleParser.initialize(body)
            Single.fromCallable {
                ScheduleResponse(
                    rawBody = body,
                    schedule = Schedule(
                        semester = scheduleParser.semester,
                        weeks = scheduleParser.weeks,
                        weekSchedule = scheduleParser.schedule
                    )
                )
            }
        }
    }
}

/*
val ScheduleEntity.rawSchedule: Schedule
    get() = GsonBuilder()
        .enableComplexMapKeySerialization()
        .create()
        .fromJson(rawScheduleStr, Schedule::class.java)
*/
