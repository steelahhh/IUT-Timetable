package com.alefimenko.iuttimetable.extension

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.alefimenko.iuttimetable.coreui.R
import com.bluelinelabs.conductor.Controller
import com.google.android.material.bottomappbar.BottomAppBar
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

fun BottomAppBar.changeMenuColors() {
    val colorOnBackground = when (AppCompatDelegate.getDefaultNightMode()) {
        MODE_NIGHT_YES ->
            context.getColorCompat(R.color.backgroundLight)
        else ->
            context.getColorCompat(R.color.backgroundDark)
    }

    for (index in 0 until menu.size()) {
        menu.getItem(index).changeIconColor(colorOnBackground)
    }

    navigationIcon = navigationIcon?.apply {
        mutate()
        setColorFilter(colorOnBackground, PorterDuff.Mode.SRC_ATOP)
    }
}

private fun MenuItem.changeIconColor(color: Int) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
        val drawable = icon
        drawable.mutate()
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        icon = drawable
    } else {
        icon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
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

fun Context.getColorCompat(id: Int) = ContextCompat.getColor(this, id)

fun Context.convertDpToPixel(dp: Float): Float {
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi / 160f)
    return Math.round(px).toFloat()
}
