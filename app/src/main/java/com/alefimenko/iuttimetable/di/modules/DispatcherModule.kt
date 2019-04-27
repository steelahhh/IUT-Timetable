package com.alefimenko.iuttimetable.di.modules

import android.os.Handler
import android.os.Looper
import org.koin.dsl.module.module
import java.util.concurrent.Executor

/*
 * Created by Alexander Efimenko on 2018-12-12.
 */

val dispatcherModule = module {
    factory {
        Executor { Handler(Looper.getMainLooper()).post(it) }
    }
}
