package com.alefimenko.iuttimetable

import android.content.Context
import android.graphics.Typeface

/*
 * Created by Alexander Efimenko on 2019-05-20.
 */

enum class Font(val path: String) {
    BOLD("manrope_bold.otf"),
    LIGHT("manrope_light.otf"),
    MEDIUM("manrope_medium.otf"),
    REGULAR("manrope_regular.otf"),
    SEMIBOLD("manrope_semibold.otf"),
    THIN("manrope_thin.otf")
}

fun Context.createTypeFace(font: Font): Typeface = Typeface.createFromAsset(assets, font.path)
