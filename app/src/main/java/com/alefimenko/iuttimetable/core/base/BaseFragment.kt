package com.alefimenko.iuttimetable.core.base

import androidx.fragment.app.Fragment
import com.alefimenko.iuttimetable.util.createBinder

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

open class BaseFragment: Fragment() {
    val bind = createBinder()
}