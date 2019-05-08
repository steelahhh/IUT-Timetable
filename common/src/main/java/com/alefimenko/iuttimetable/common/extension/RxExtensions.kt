package com.alefimenko.iuttimetable.common.extension

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/*
 * Created by Alexander Efimenko on 21/11/18.
 */

fun Completable.ioMainSchedulers() = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun Completable.ioSchedulers() = this
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.io())

fun <T> Observable<T>.ioMainSchedulers() = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.ioSchedulers() = this
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.io())

fun <T> Single<T>.ioMainSchedulers() = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.ioSchedulers() = this
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.io())

fun <T> Maybe<T>.ioMainSchedulers() = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Maybe<T>.ioSchedulers() = this
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.io())

fun <T, R> Observable<List<T>>.mapList(
    mapper: (t: T) -> R
): Observable<List<R>> = map { list ->
    list.map(mapper)
}
