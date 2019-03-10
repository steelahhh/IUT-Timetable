package com.alefimenko.iuttimetable.feature.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseController

/*
 * Created by Alexander Efimenko on 2019-03-08.
 */

class ScheduleFragment(val text: String) : BaseController<String, String>() {

    constructor() : this("")

    private val textView by bind<TextView>(R.id.schedule_text)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        textView.text = text
    }

    override fun accept(t: String?) {
    }
}