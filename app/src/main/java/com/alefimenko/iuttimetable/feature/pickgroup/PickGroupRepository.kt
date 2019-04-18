package com.alefimenko.iuttimetable.feature.pickgroup

import android.util.LruCache
import com.alefimenko.iuttimetable.core.data.NetworkStatusReceiver
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.core.data.local.SchedulesDao
import com.alefimenko.iuttimetable.core.data.local.model.ScheduleEntity
import com.alefimenko.iuttimetable.core.data.remote.Exceptions
import com.alefimenko.iuttimetable.core.data.remote.FeedbackService
import com.alefimenko.iuttimetable.core.data.remote.ScheduleService
import com.alefimenko.iuttimetable.core.data.remote.model.toUi
import com.alefimenko.iuttimetable.core.data.remote.toFormPath
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.feature.schedule.model.GroupInfo
import com.alefimenko.iuttimetable.util.ioMainSchedulers
import io.reactivex.Completable
import io.reactivex.Observable

/*
 * Created by Alexander Efimenko on 2019-02-14.
 */

class PickGroupRepository(
    private val preferences: LocalPreferences,
    private val scheduleService: ScheduleService,
    private val feedbackService: FeedbackService,
    private val schedulesDao: SchedulesDao,
    private val networkStatusReceiver: NetworkStatusReceiver
) {

    private val lruCache: LruCache<String, List<InstituteUi>> = LruCache(2 * 1024 * 1024)

    fun getGroups(form: Int, instituteId: Int): Observable<List<GroupUi>> {
        return if (networkStatusReceiver.isNetworkAvailable()) {
            scheduleService.fetchGroups(form.toFormPath(), instituteId)
                .toObservable()
                .ioMainSchedulers()
                .map { groups ->
                    groups.map { group ->
                        group.toUi()
                    }
                }
        } else {
            Observable.error(Exceptions.NoNetworkException())
        }
    }

    fun getInstitutes(): Observable<List<InstituteUi>> {
        val cachedInstitutes = lruCache[INSTITUTES]
        return if (cachedInstitutes != null) {
            Observable.just(cachedInstitutes)
        } else {
            if (networkStatusReceiver.isNetworkAvailable()) {
                scheduleService.fetchInstitutes()
                    .toObservable()
                    .ioMainSchedulers()
                    .map { institutes ->
                        institutes.map { institute ->
                            InstituteUi.fromResponse(institute)
                        }.also {
                            lruCache.put(INSTITUTES, it)
                        }
                    }
            } else {
                Observable.error(Exceptions.NoNetworkException())
            }
        }
    }

    fun changeTheme() {
        preferences.isNightMode = preferences.isNightMode.not()
    }

    fun saveGroup(groupInfo: GroupInfo): Completable = schedulesDao.insert(
        ScheduleEntity(
            formId = groupInfo.form,
            groupName = groupInfo.group.label,
            groupId = groupInfo.group.id,
            instituteName = groupInfo.institute.label,
            instituteId = groupInfo.institute.id
        )
    ).also {
        preferences.currentGroup = groupInfo.group.id
    }

    companion object {
        const val INSTITUTES = "INSTITUTES"
        const val GROUPS = "GROUPS"
    }
}
