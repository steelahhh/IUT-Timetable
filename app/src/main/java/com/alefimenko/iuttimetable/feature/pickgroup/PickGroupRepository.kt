package com.alefimenko.iuttimetable.feature.pickgroup

import android.util.LruCache
import com.alefimenko.iuttimetable.schedule.SchedulesDao
import com.alefimenko.iuttimetable.core.data.NetworkStatusReceiver
import com.alefimenko.iuttimetable.Preferences
import com.alefimenko.iuttimetable.core.data.remote.Exceptions
import com.alefimenko.iuttimetable.core.data.remote.FeedbackService
import com.alefimenko.iuttimetable.core.data.remote.ScheduleService
import com.alefimenko.iuttimetable.core.data.remote.model.toGroupUi
import com.alefimenko.iuttimetable.core.data.remote.model.toInstituteUi
import com.alefimenko.iuttimetable.core.data.remote.toFormPath
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.alefimenko.iuttimetable.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.extension.mapList
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
