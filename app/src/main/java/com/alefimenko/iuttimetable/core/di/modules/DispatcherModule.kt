package com.alefimenko.iuttimetable.core.di.modules

import android.os.Handler
import android.os.Looper
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import javax.inject.Named

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

@Module
object DispatcherModule {
    @Provides
    @JvmStatic
    @Named("dispatcher")
    fun provideMainThreadExecutor(): Executor {
        val handler = Handler(Looper.getMainLooper())
        return Executor { handler.post(it) }
    }
}