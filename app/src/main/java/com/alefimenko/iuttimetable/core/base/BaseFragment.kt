package com.alefimenko.iuttimetable.core.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alefimenko.iuttimetable.util.createBinder

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

open class BaseFragment: Fragment() {

    val bind = createBinder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as BaseActivity).updateNavigationColor()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bind.resetViews()
    }

}
