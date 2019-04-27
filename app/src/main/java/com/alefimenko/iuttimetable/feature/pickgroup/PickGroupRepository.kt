package com.alefimenko.iuttimetable.feature.pickgroup

import android.util.LruCache
import com.alefimenko.iuttimetable.local.Preferences
import com.alefimenko.iuttimetable.remote.Exceptions
import com.alefimenko.iuttimetable.toGroupUi
import com.alefimenko.iuttimetable.toInstituteUi
import com.alefimenko.iuttimetable.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.extension.mapList
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.remote.FeedbackService
import com.alefimenko.iuttimetable.NetworkStatusReceiver
import com.alefimenko.iuttimetable.remote.ScheduleService
import com.alefimenko.iuttimetable.remote.toFormPath
import com.alefimenko.iuttimetable.local.schedule.SchedulesDao
import io.reactivex.Observable

/*
 * Created by Alexander Efimenko on 2019-02-14.
 */

class PickGroupRepository(
    private val preferences: Preferences,
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
                .mapList { group ->
                    group.toGroupUi()
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
                            institute.toInstituteUi()
                        }.also {
                            lruCache.put(INSTITUTES, it)
                        }
                    }
            } else {
                Observable.error(Exceptions.NoNetworkException())
            }
        }
    }

    companion object {
        const val INSTITUTES = "INSTITUTES"
        const val GROUPS = "GROUPS"
    }
}
