package com.alefimenko.iuttimetable.data

import android.content.Context
import androidx.room.Room
import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.data.local.Preferences
import com.alefimenko.iuttimetable.data.local.schedule.GroupsDao
import com.alefimenko.iuttimetable.data.local.schedule.InstitutesDao
import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDao
import com.alefimenko.iuttimetable.data.local.schedule.SchedulesDatabase
import com.alefimenko.iuttimetable.data.remote.ScheduleService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import javax.inject.Named
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/*
 * Author: steelahhh
 * 21/3/20
 */

@dagger.Module
object DataModule {
    private const val BASE_URL = "https://www.tyuiu.ru/"
    private const val CONNECT_TIMEOUT_IN_MS = 1500L

    @dagger.Provides
    fun parser(): ScheduleParser = ScheduleParser()

    @dagger.Provides
    fun provideGson(): Gson = GsonBuilder().enableComplexMapKeySerialization().create()

    @dagger.Provides
    fun preferences(context: Context): Preferences = Preferences(
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
    )

    @dagger.Provides
    fun room(context: Context): SchedulesDatabase = Room.databaseBuilder(
        context,
        SchedulesDatabase::class.java, "Schedules"
    ).fallbackToDestructiveMigration().build()

    @dagger.Provides
    @Named(GroupsDao.TAG)
    fun groupsDao(database: SchedulesDatabase): GroupsDao = database.groupsDao

    @dagger.Provides
    @Named(InstitutesDao.TAG)
    fun instituteDao(database: SchedulesDatabase): InstitutesDao = database.institutesDao

    @dagger.Provides
    @Named(SchedulesDao.TAG)
    fun schedulesDao(database: SchedulesDatabase): SchedulesDao = database.schedulesDao

    @dagger.Provides
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
        .readTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(true)
        .build()

    @dagger.Provides
    fun provideService(okHttpClient: OkHttpClient): ScheduleService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
        .create(ScheduleService::class.java)
}
