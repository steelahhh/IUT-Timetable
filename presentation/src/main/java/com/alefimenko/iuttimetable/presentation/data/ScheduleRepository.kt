package com.alefimenko.iuttimetable.presentation.data

import com.alefimenko.iuttimetable.common.NetworkStatusReceiver
import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.data.GroupInfo
import com.alefimenko.iuttimetable.data.ScheduleParser
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.local.model.GroupEntity
import com.alefimenko.iuttimetable.data.local.model.ScheduleEntity
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.data.local.schedule.InstitutesDao
import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDao
import com.alefimenko.iuttimetable.data.remote.Exceptions
import com.alefimenko.iuttimetable.data.remote.ScheduleService
import com.alefimenko.iuttimetable.data.remote.model.ClassEntry
import com.alefimenko.iuttimetable.data.remote.model.Schedule
import com.alefimenko.iuttimetable.data.remote.model.ScheduleResponse
import com.alefimenko.iuttimetable.data.remote.model.WeekSchedule
import com.alefimenko.iuttimetable.data.remote.toFormPath
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import javax.inject.Inject
import javax.inject.Named

/*
 * Created by Alexander Efimenko on 2019-03-13.
 */

class ScheduleRepository @Inject constructor(
    private val preferences: Preferences,
    private val gson: Gson,
    private val scheduleParser: ScheduleParser,
    private val scheduleService: ScheduleService,
    @Named(SchedulesDao.TAG)
    private val schedulesDao: SchedulesDao,
    @Named(GroupsDao.TAG)
    private val groupsDao: GroupsDao,
    @Named(InstitutesDao.TAG)
    private val instituteDao: InstitutesDao,
    private val networkStatusReceiver: NetworkStatusReceiver
) {
    val currentGroup: Int get() = preferences.currentGroup

    val shouldSwitchToDay: Boolean get() = preferences.switchDay

    fun getGroups() = groupsDao.getGroupsWithInstitute().ioMainSchedulers()

    fun getSchedule(groupId: Int): Observable<Schedule> = schedulesDao
        .getByGroupId(groupId)
        .zipWith(groupsDao.getById(groupId))
        .map { (scheduleEntity, group) ->
            scheduleEntity.schedule.copy(groupTitle = group.name)
        }
        .flatMapObservable {
            saveCurrentGroup(groupId).toSingleDefault(it).toObservable()
        }
        .ioMainSchedulers()

    fun downloadSchedule(groupInfo: GroupInfo): Observable<Schedule> {
        val formPath = groupInfo.form.toFormPath()
        return if (networkStatusReceiver.isNetworkAvailable()) {
            scheduleService.fetchSchedule(formPath, groupInfo.group.id)
                .flatMap { body -> createSchedule(body.string(), groupInfo.group.name) }
                .flatMap { response -> saveSchedule(groupInfo, response).toSingleDefault(response.schedule) }
                .toObservable()
                .ioMainSchedulers()
        } else {
            Observable.error(Exceptions.NoNetworkException())
        }
    }

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

    fun hideClassAndUpdate(classIndex: Int, dayIndex: Int, weekIndex: Int): Observable<Schedule> =
        schedulesDao.getByGroupId(preferences.currentGroup)
            .zipWith(groupsDao.getById(preferences.currentGroup))
            .flatMapObservable { (scheduleEntity, group) ->
                val schedule = scheduleEntity.schedule.copy(groupTitle = group.name)

                val newSchedule = schedule.copy(
                    weekSchedule = schedule.createWithHiddenClass(weekIndex, dayIndex, classIndex)
                )

                schedulesDao.insert(scheduleEntity.copy(scheduleStr = gson.toJson(newSchedule)))
                    .toSingleDefault(newSchedule).toObservable()
            }

    private fun Schedule.createWithHiddenClass(
        weekIndex: Int,
        dayIndex: Int,
        classIndex: Int
    ): MutableMap<Int, WeekSchedule> {
        val newWeeksSchedule = mutableMapOf<Int, WeekSchedule>()
        // Go over all the weeks, and hide/show the selected one
        weekSchedule.keys.forEach { week ->
            val newDays: MutableList<List<ClassEntry>> = mutableListOf()
            weekSchedule.getValue(week).forEachIndexed { day, weekSchedule ->
                newDays.add(weekSchedule.mapIndexed { index, classEntry ->
                    val shouldChangeVisibility =
                        week == weekIndex && day == dayIndex && index == classIndex
                    if (shouldChangeVisibility) {
                        classEntry.copy(hidden = !classEntry.hidden)
                    } else {
                        classEntry
                    }
                })
            }
            newWeeksSchedule[week] = newDays
        }
        return newWeeksSchedule
    }

    private fun saveCurrentGroup(groupId: Int) = Completable.fromAction {
        preferences.currentGroup = groupId
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

    private val ScheduleEntity.schedule: Schedule
        get() = gson.fromJson(scheduleStr, Schedule::class.java)

    private val ScheduleEntity.rawSchedule: Schedule
        get() = gson.fromJson(rawScheduleStr, Schedule::class.java)
}
