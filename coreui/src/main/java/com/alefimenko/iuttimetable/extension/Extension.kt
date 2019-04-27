package com.alefimenko.iuttimetable.extension

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bluelinelabs.conductor.Controller
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*
 * Created by Alexander Efimenko on 2019-04-24.
 */

fun FloatingActionButton.show(state: Boolean) {
    if (state) {
        show()
    } else {
        hide()
    }
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun RadioGroup.changeEnabled(enabled: Boolean) {
    for (child in 0..childCount) {
        (getChildAt(child) as? RadioButton)?.isEnabled = enabled
        (getChildAt(child) as? RadioButton)?.isClickable = enabled
    }
}

fun Controller.requireView() = view ?: error("There is no view bound to this controller")

fun Controller.requireContext() = requireView().context

fun Controller.requireActivity() = activity ?: error("There is no activity bound to this controller")

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
