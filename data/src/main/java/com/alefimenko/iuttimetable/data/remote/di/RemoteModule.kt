package com.alefimenko.iuttimetable.data.remote.di

import com.alefimenko.iuttimetable.data.remote.FeedbackService
import com.alefimenko.iuttimetable.common.NetworkStatusReceiver
import com.alefimenko.iuttimetable.data.remote.ScheduleService
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
 * Created by Alexander Efimenko on 2019-04-27.
 */

private const val BASE_URL = "https://www.tyuiu.ru/"
private const val CONNECT_TIMEOUT_IN_MS = 1500L

val remoteModule = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
            .readTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get())
            .build()
            .create(ScheduleService::class.java)
    }

    single {
        FeedbackService(androidContext())
    }

    single {
        NetworkStatusReceiver(androidContext())
    }
}
