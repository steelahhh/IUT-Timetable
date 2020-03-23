package com.alefimenko.iuttimetable.common

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/*
 * Author: steelahhh
 * 23/3/20
 */

open class EmptyObserver<T> : Observer<T> {
    override fun onComplete() = Unit
    override fun onSubscribe(d: Disposable) = Unit
    override fun onNext(value: T) = Unit
    override fun onError(error: Throwable) = error.printStackTrace()
}
