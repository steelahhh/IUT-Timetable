package com.alefimenko.iuttimetable.core.di.modules

import com.alefimenko.iuttimetable.BuildConfig
import com.alefimenko.iuttimetable.core.data.remote.ScheduleService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Module
class NetworkModule {

    @Singleton
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_IN_MS.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(CONNECT_TIMEOUT_IN_MS.toLong(), TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Singleton
    @Provides
    internal fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    internal fun scheduleService(retrofit: Retrofit): ScheduleService {
        return retrofit.create(ScheduleService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://www.tyuiu.ru/"
        private const val CONNECT_TIMEOUT_IN_MS = 30000
    }
}