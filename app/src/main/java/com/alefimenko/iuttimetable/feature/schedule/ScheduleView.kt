package com.alefimenko.iuttimetable.feature.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseController
import com.alefimenko.iuttimetable.core.base.KotlinView
import com.alefimenko.iuttimetable.feature.schedule.schedulepage.SchedulePagerAdapter
import com.alefimenko.iuttimetable.util.requireActivity
import kotlinx.android.synthetic.main.fragment_schedule.*

/*
 * Created by Alexander Efimenko on 2019-04-21.
 */

class ScheduleView(
    inflater: LayoutInflater,
    container: ViewGroup,
    private val host: BaseController<*, *>,
    private val pagerAdapter: SchedulePagerAdapter
) : KotlinView(R.layout.fragment_schedule, inflater, container) {
    fun setup() {
        viewPager.adapter = pagerAdapter
        scheduleTabs.setupWithViewPager(viewPager)
    }

    override fun tearDown() {
        if (!host.requireActivity().isChangingConfigurations) {
            viewPager.adapter = null
        }
        scheduleTabs.setupWithViewPager(null)
        super.tearDown()
    }
}
