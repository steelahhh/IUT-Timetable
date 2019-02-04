package com.alefimenko.iuttimetable.core.di.modules

import android.content.Context
import com.alefimenko.iuttimetable.core.data.NetworkStatusReceiver
import com.alefimenko.iuttimetable.core.data.remote.FeedbackService
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
object NetworkModule {

    private const val BASE_URL = "https://www.tyuiu.ru/"
    private const val CONNECT_TIMEOUT_IN_MS = 3000L

    @Singleton
    @Provides
    @JvmStatic
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
        .readTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(true)
        .build()


    @Singleton
    @Provides
    @JvmStatic
    fun scheduleService(okHttpClient: OkHttpClient): ScheduleService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
        .create(ScheduleService::class.java)


    @Singleton
    @Provides
    @JvmStatic
    fun provideFeedbackService(context: Context) = FeedbackService(context)

    @Singleton
    @Provides
    @JvmStatic
    fun provideNetworkReceiver(context: Context) = NetworkStatusReceiver(context)

}