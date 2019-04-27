package com.alefimenko.iuttimetable.feature.schedule.schedulepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.DateInteractor
import com.alefimenko.iuttimetable.base.BaseController
import org.koin.android.ext.android.inject

/*
 * Created by Alexander Efimenko on 2019-04-18.
 */

class SchedulePageController(
    val position: Int = 0
) : BaseController<String, String>() {
    private val dateInteractorImpl: DateInteractor by inject()

    private val text by bind<TextView>(R.id.text)

    override fun acceptViewModel(viewModel: String) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.fragment_schedule_page, container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        text.text = "$position page ${if (dateInteractorImpl.isWeekOdd) "Нечетная" else "четная"}"
    }
}
