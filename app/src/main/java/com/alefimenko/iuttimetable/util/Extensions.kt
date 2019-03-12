package com.alefimenko.iuttimetable.util

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
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

fun RadioGroup.changeEnabled(enabled: Boolean) {
    for (child in 0..childCount) {
        (getChildAt(child) as? RadioButton)?.isEnabled = enabled
        (getChildAt(child) as? RadioButton)?.isClickable = enabled
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

fun Context.convertDpToPixel(dp: Float): Float {
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi / 160f)
    return Math.round(px).toFloat()
}

inline fun <reified T : Any, F : Fragment> F.getArg(key: String): T {
    return (arguments?.get(key) ?: NullPointerException(
        "No argument for key [$key] in ${this::class.java.simpleName}"
    )) as T
}

inline fun <reified T : Any, F : Fragment> F.arg(key: String) = lazy { getArg<T, F>(key) }

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
