package com.alefimenko.iuttimetable.presentation.schedule.model

import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.presentation.R
import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.KlockLocale
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_simple_text.view.*

/*
 * Created by Alexander Efimenko on 2019-05-16.
 */

data class HeaderItem(
    val day: Int,
    val isToday: Boolean = false,
    val date: String = Constants.EMPTY_ENTRY
) : Item(day.toLong()) {
    override fun getLayout() = R.layout.item_simple_text

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder.itemView) {
        val dayIdx = when (KlockLocale.default) {
            is KlockLocale.English -> day + 1
            else -> day
        }
        val dayText = DayOfWeek[dayIdx].localName(KlockLocale.default).capitalize()

        title.text = if (date != Constants.EMPTY_ENTRY) {
            context.getString(R.string.class_header_date_title, dayText, date)
        } else {
            dayText
        }
    }
}
