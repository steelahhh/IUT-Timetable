package com.alefimenko.iuttimetable.settings.data

import com.alefimenko.iuttimetable.data.GroupInfo
import com.alefimenko.iuttimetable.data.ScheduleParser
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.local.model.GroupEntity
import com.alefimenko.iuttimetable.data.local.model.ScheduleEntity
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.data.local.schedule.InstitutesDao
import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDao
import com.alefimenko.iuttimetable.data.remote.FeedbackService
import com.alefimenko.iuttimetable.data.remote.ScheduleService
import com.alefimenko.iuttimetable.data.remote.model.Schedule
import com.alefimenko.iuttimetable.data.remote.model.ScheduleResponse
import com.alefimenko.iuttimetable.data.remote.toFormPath
import com.alefimenko.iuttimetable.data.toDb
import com.alefimenko.iuttimetable.data.toDomain
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import javax.inject.Inject
import javax.inject.Named

/*
 * Created by Alexander Efimenko on 2019-07-17.
 */

class SettingsRepository @Inject constructor(
    private val scheduleService: ScheduleService,
    private val preferences: Preferences,
    @Named(SchedulesDao.TAG)
    private val schedulesDao: SchedulesDao,
    @Named(GroupsDao.TAG)
    private val groupsDao: GroupsDao,
    @Named(InstitutesDao.TAG)
    private val instituteDao: InstitutesDao,
    private val scheduleParser: ScheduleParser,
    private val gson: Gson
) {
    fun updateCurrentSchedule(groupId: Int = preferences.currentGroup): Observable<Boolean> {
        return groupsDao.getById(groupId).flatMap { group ->
            Maybe.fromCallable { group }.zipWith(instituteDao.getById(group.instituteId))
        }
            .flatMapObservable { (group, institute) ->
                val groupInfo = GroupInfo(
                    form = group.form,
                    group = group.toDomain(),
                    institute = institute.toDomain()
                )
                val formPath = groupInfo.form.toFormPath()
                scheduleService.fetchSchedule(formPath, groupInfo.group.id)
                    .flatMap { body -> createSchedule(body.string(), groupInfo.group.name) }
                    .toObservable()
                    .zipWith(schedulesDao.getByGroupId(groupId).toObservable())
                    .flatMap { (scheduleResponse, scheduleEntity) ->
                        val areSchedulesSame = scheduleResponse.rawBody == scheduleEntity.rawScheduleStr
                        when {
                            !areSchedulesSame -> saveSchedule(
                                groupInfo,
                                scheduleResponse
                            ).toSingleDefault(true).toObservable()
                            else -> Observable.just(scheduleResponse.rawBody != scheduleEntity.rawScheduleStr)
                        }
                    }
            }
    }

    fun getFeedbackInfo(): Observable<FeedbackService.FeedbackInfo> =
        groupsDao.getById(preferences.currentGroup)
            .flatMap { group ->
                Maybe.just(group).zipWith(instituteDao.getById(group.instituteId))
            }
            .map { (group, institute) ->
                FeedbackService.FeedbackInfo(
                    group.form,
                    group.id,
                    group.name,
                    institute.id,
                    institute.name
                )
            }
            .toObservable()

    private fun createSchedule(body: String, groupTitle: String): Single<ScheduleResponse> {
        return Single.defer {
            scheduleParser.initialize(body)
            Single.fromCallable {
                ScheduleResponse(
                    rawBody = body,
                    schedule = Schedule(
                        groupTitle = groupTitle,
                        semester = scheduleParser.semester,
                        weeks = scheduleParser.weeks,
                        weekSchedule = scheduleParser.schedule
                    )
                )
            }
        }
    }

    private fun saveSchedule(
        groupInfo: GroupInfo,
        response: ScheduleResponse
    ) = with(groupInfo) {
        schedulesDao.insert(
            ScheduleEntity(
                id = group.id.toLong(),
                groupId = group.id,
                scheduleStr = gson.toJson(response.schedule),
                rawScheduleStr = response.rawBody
            )
        ).andThen(
            groupsDao.insert(
                GroupEntity(
                    group.id,
                    group.name,
                    form,
                    institute.id,
                    response.schedule.semester
                )
            )
        ).andThen(
            instituteDao.insert(institute.toDb())
        ).andThen(
            saveCurrentGroup(groupInfo.group.id)
        )
    }

    private fun saveCurrentGroup(groupId: Int) = Completable.fromAction {
        preferences.currentGroup = groupId
    }
}
