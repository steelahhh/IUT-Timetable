package com.alefimenko.iuttimetable.feature.schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseController
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.core.data.local.SchedulesDao
import com.alefimenko.iuttimetable.feature.schedule.model.GroupInfo
import com.alefimenko.iuttimetable.util.ioMainSchedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 2019-03-08.
 */

class ScheduleController(
    private val bundle: Bundle = Bundle()
) : BaseController<String, String>() {
    private val textView by bind<TextView>(R.id.schedule_text)

    private val groupsDao: SchedulesDao by inject()
    private val prefs: LocalPreferences by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onAttach(view: View) {
        super.onAttach(view)
        textView.text = "${bundle[GROUP_INFO_KEY] as? GroupInfo}"
        if (bundle[GROUP_INFO_KEY] == null) {
            groupsDao.getByGroupId(prefs.currentGroup).ioMainSchedulers().subscribe({
                textView.text = it.toString()
                    .replace(",".toRegex(), ",\n")
                    .replace("\\(".toRegex(), "\\(\n")
                    .replace("\\)".toRegex(), "\n\\)")
            }, {
                Timber.e(it)
            })
        }
    }

    override fun acceptViewmodel(viewmodel: String) {
    }

    companion object {
        const val TAG = "ScheduleController"

        const val GROUP_INFO_KEY = "GROUP_INFO_KEY"

        fun newInstance(groupInfo: GroupInfo) = ScheduleController(Bundle().apply {
            putParcelable(GROUP_INFO_KEY, groupInfo)
        })
    }
}
