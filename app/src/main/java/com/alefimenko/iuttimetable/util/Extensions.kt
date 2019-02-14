package com.alefimenko.iuttimetable.util

import android.graphics.Typeface
import android.os.Build
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/*
 * Created by Alexander Efimenko on 21/11/18.
 */

fun FloatingActionButton.show(state: Boolean) {
    if (state) {
        show()
    } else {
        hide()
    }
}

fun Toolbar.changeToolbarFont() {
    for (i in 0 until childCount) {
        val view = getChildAt(i)
        if (view is TextView && view.text == title) {
            view.typeface = Typeface.createFromAsset(view.context.assets, "manrope_bold.otf")
            break
        }
    }
}

inline fun withLollipop(action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        action()
    }
}

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
