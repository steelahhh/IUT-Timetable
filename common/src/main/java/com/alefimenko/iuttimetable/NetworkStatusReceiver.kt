package com.alefimenko.iuttimetable

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/*
 * Created by Alexander Efimenko on 2019-01-20.
 */

class NetworkStatusReceiver(
    private val applicationContext: Context
) : BroadcastReceiver() {

    private val networkInfoSubject = BehaviorSubject.create<Boolean>()

    override fun onReceive(context: Context, intent: Intent?) {
        networkInfoSubject.onNext(context.isConnected())
    }

    fun asObservable(): Observable<Boolean> = networkInfoSubject.hide()

    fun asSingle(): Single<Boolean> = networkInfoSubject.singleOrError()

    fun isNetworkAvailable(): Boolean = networkInfoSubject.value ?: applicationContext.isConnected()

    private fun Context.isConnected() =
        (applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .activeNetworkInfo?.isConnected == true
}
