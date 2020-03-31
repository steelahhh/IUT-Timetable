package com.alefimenko.iuttimetable.presentation.data

import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDao
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

/*
 * Author: steelahhh
 * 31/3/20
 */

class GroupsRepository @Inject constructor(
    @Named(GroupsDao.TAG)
    private val groupsDao: GroupsDao,
    @Named(SchedulesDao.TAG)
    private val schedulesDao: SchedulesDao,
    val preferences: Preferences
) {
    val currentGroup get() = preferences.currentGroup

    fun getGroups() = groupsDao.getGroupsWithInstitute()

    fun deleteCurrentSchedule(
        groupId: Int
    ) = Single.fromCallable {
        preferences.currentGroup = Constants.ITEM_DOESNT_EXIST
        groupsDao.delete(groupId)
        schedulesDao.deleteByGroupId(groupId)
    }.subscribeOn(Schedulers.io()).flatMap {
        schedulesDao.schedules
            .flatMap { schedules ->
                Single.just(
                    schedules.firstOrNull { it.groupId != groupId }?.groupId ?: -1
                )
            }
    }

    fun deleteSchedule(groupId: Int) = Completable.fromCallable {
        groupsDao.delete(groupId)
        schedulesDao.deleteByGroupId(groupId)
    }
}
