package com.alefimenko.iuttimetable.util

import android.graphics.Typeface
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
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

fun Disposable.addTo(autoDisposable: AutoDisposable) {
    autoDisposable.add(this)
}