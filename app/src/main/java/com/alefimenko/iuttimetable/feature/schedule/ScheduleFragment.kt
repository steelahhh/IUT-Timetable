package com.alefimenko.iuttimetable.feature.schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseController
import com.alefimenko.iuttimetable.core.data.local.GroupsDao
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.util.ioMainSchedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

/*
 * Created by Alexander Efimenko on 2019-03-08.
 */

class ScheduleFragment(val text: String) : BaseController<String, String>() {

    constructor() : this("")

    private val textView by bind<TextView>(R.id.schedule_text)

    private val groupsDao: GroupsDao by inject()
    private val prefs: LocalPreferences by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onAttach(view: View) {
        super.onAttach(view)
        textView.text = text
        groupsDao.groups.ioMainSchedulers().subscribe({
            textView.text = it.find { it.id == prefs.currentGroup }?.toString()
        }, {
            Timber.e(it)
        })
    }

    override fun accept(t: String?) {
    }
}