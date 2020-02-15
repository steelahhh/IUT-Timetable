package com.alefimenko.iuttimetable.presentation.schedule.model

import com.alefimenko.iuttimetable.presentation.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_schedule_header.*

/*
 * Created by Alexander Efimenko on 2019-07-09.
 */

data class ScheduleInfoHeader(
    val group: String,
    val semester: String,
    val isWeekOdd: Boolean
) : Item() {
    override fun getLayout(): Int = R.layout.item_schedule_header

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder) {
        scheduleHeaderTitle.text = group
        scheduleHeaderSubtitle.text = semester
    }
}
