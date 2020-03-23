package com.alefimenko.iuttimetable.presentation.pickgroup

import android.util.LruCache
import com.alefimenko.iuttimetable.common.NetworkStatusReceiver
import com.alefimenko.iuttimetable.common.extension.ioMainSchedulers
import com.alefimenko.iuttimetable.data.Institute
import com.alefimenko.iuttimetable.data.remote.Exceptions
import com.alefimenko.iuttimetable.data.remote.ScheduleService
import com.alefimenko.iuttimetable.data.remote.model.IUTLabeledResponse
import com.alefimenko.iuttimetable.data.remote.toFormPath
import com.alefimenko.iuttimetable.presentation.model.toInstitute
import io.reactivex.Observable
import javax.inject.Inject

/*
 * Created by Alexander Efimenko on 2019-02-14.
 */

class PickGroupRepository @Inject constructor(
    private val scheduleService: ScheduleService,
    private val networkStatusReceiver: NetworkStatusReceiver
) {

    private val lruCache: LruCache<String, List<Institute>> = LruCache(2 * 1024 * 1024)

    fun getGroups(form: Int, instituteId: Int): Observable<List<IUTLabeledResponse>> {
        return if (networkStatusReceiver.isNetworkAvailable()) {
            scheduleService.fetchGroups(form.toFormPath(), instituteId)
                .toObservable()
                .ioMainSchedulers()
        } else {
            Observable.error(Exceptions.NoNetworkException())
        }
    }

    fun getInstitutes(): Observable<List<Institute>> {
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
                            institute.toInstitute()
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
