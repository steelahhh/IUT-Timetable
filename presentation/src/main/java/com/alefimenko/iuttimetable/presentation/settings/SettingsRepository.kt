package com.alefimenko.iuttimetable.presentation.settings

import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.data.local.schedule.InstitutesDao
import com.alefimenko.iuttimetable.data.remote.FeedbackService
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith
import javax.inject.Inject
import javax.inject.Named

/*
 * Created by Alexander Efimenko on 2019-07-17.
 */

class SettingsRepository @Inject constructor(
    private val preferences: Preferences,
    @Named(GroupsDao.TAG)
    private val groupsDao: GroupsDao,
    @Named(InstitutesDao.TAG)
    private val instituteDao: InstitutesDao
) {
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
}
