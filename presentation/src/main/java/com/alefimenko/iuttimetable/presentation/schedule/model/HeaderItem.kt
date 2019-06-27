package com.alefimenko.iuttimetable.presentation.schedule.model

import com.alefimenko.iuttimetable.presentation.R
import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.KlockLocale
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_simple_text.view.*

/*
 * Created by Alexander Efimenko on 2019-05-16.
 */

data class HeaderItem(val day: Int, val isToday: Boolean = false) : Item(day.toLong()) {
    override fun getLayout() = R.layout.item_simple_text

    override fun bind(viewHolder: ViewHolder, position: Int) = with(viewHolder.itemView) {
        val dayIdx = when (KlockLocale.default) {
            is KlockLocale.English -> day + 1
            else -> day
        }
        title.text = DayOfWeek[dayIdx].localName(KlockLocale.default).capitalize()
    }
}
