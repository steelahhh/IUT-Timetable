package com.alefimenko.iuttimetable.util

import android.graphics.Typeface
import android.os.Build
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import com.alefimenko.iuttimetable.core.arch.AutoDisposable
import io.reactivex.disposables.Disposable

/*
 * Created by Alexander Efimenko on 21/11/18.
 */

fun Toolbar.changeToolbarFont() {
    for (i in 0 until childCount) {
        val view = getChildAt(i)
        if (view is TextView && view.text == title) {
            view.typeface = Typeface.createFromAsset(view.context.assets, "manrope_bold.otf")
            break
        }
    }
}

fun <T> MutableLiveData<T>.default(value: T): MutableLiveData<T> {
    this.value = value
    return this
}


inline fun withLollipop(action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        action()
    }
}

fun Disposable.addTo(autoDisposable: AutoDisposable) {
    autoDisposable.add(this)
}