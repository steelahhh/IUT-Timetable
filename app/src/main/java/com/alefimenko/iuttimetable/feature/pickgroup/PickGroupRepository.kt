package com.alefimenko.iuttimetable.feature.pickgroup

import android.util.LruCache
import com.alefimenko.iuttimetable.core.data.NetworkStatusReceiver
import com.alefimenko.iuttimetable.core.data.local.GroupsDao
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.core.data.remote.FeedbackService
import com.alefimenko.iuttimetable.core.data.remote.NoNetworkException
import com.alefimenko.iuttimetable.core.data.remote.ScheduleService
import com.alefimenko.iuttimetable.core.data.remote.toFormPath
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.toEntity
import com.alefimenko.iuttimetable.model.mappers.ScheduleParser
import com.alefimenko.iuttimetable.model.toUi
import com.alefimenko.iuttimetable.util.ioMainSchedulers
import io.reactivex.Completable
import io.reactivex.Observable

/*
 * Created by Alexander Efimenko on 2019-02-14.
 */

class PickGroupRepository(
    private val preferences: LocalPreferences,
    private val scheduleParser: ScheduleParser,
    private val scheduleService: ScheduleService,
    private val feedbackService: FeedbackService,
    private val groupsDao: GroupsDao,
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
            Observable.error(NoNetworkException())
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
                Observable.error(NoNetworkException())
            }
        }
    }

    fun changeTheme() {
        preferences.isNightMode = preferences.isNightMode.not()
    }

    fun saveGroup(group: GroupUi): Completable = groupsDao.insertGroup(group.toEntity()).also {
        preferences.currentGroup = group.id
    }

    companion object {
        const val INSTITUTES = "INSTITUTES"
        const val GROUPS = "GROUPS"
    }
}
