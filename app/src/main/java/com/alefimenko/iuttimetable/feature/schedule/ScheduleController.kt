package com.alefimenko.iuttimetable.feature.schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alefimenko.iuttimetable.core.base.BaseController
import com.alefimenko.iuttimetable.feature.schedule.model.GroupInfo
import com.alefimenko.iuttimetable.feature.schedule.schedulepage.SchedulePagerAdapter

/*
 * Created by Alexander Efimenko on 2019-03-08.
 */

class ScheduleController(
    private val bundle: Bundle = Bundle()
) : BaseController<String, String>() {
    private val pagerAdapter by bind.stuff {
        SchedulePagerAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = ScheduleView(inflater, container, this, pagerAdapter)
        view.setup()
        return view.containerView ?: error(":(")
    }

    @SuppressLint("CheckResult")
    override fun onAttach(view: View) {
        super.onAttach(view)
        val groupInfo = bundle[GROUP_INFO] as? GroupInfo
    }

    override fun acceptViewModel(viewModel: String) {
    }

    companion object {
        const val TAG = "ScheduleController"
        const val SELECTED_DAY = "selected_day"
        private const val GROUP_INFO = "GROUP_INFO_KEY"

        fun newInstance(groupInfo: GroupInfo) = ScheduleController(
            Bundle().apply {
                putParcelable(GROUP_INFO, groupInfo)
            }
        )
    }
}
