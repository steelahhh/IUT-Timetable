package com.alefimenko.iuttimetable.di

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import org.koin.dsl.module

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val dispatcherModule = module {
    factory {
        Executor { Handler(Looper.getMainLooper()).post(it) }
    }
}
