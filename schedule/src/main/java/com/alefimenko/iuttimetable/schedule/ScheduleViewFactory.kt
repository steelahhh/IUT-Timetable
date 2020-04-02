package com.alefimenko.iuttimetable.schedule

import android.view.ViewGroup
import com.badoo.ribs.customisation.inflate

class ScheduleViewFactory : ScheduleView.Factory {
    override fun invoke(deps: Boolean?): (ViewGroup) -> ScheduleView = {
//        ScheduleViewListImpl(inflate(it, R.layout.rib_schedule_list))
        ScheduleViewTabsImpl(inflate(it, R.layout.rib_schedule_tabs))
    }
}
