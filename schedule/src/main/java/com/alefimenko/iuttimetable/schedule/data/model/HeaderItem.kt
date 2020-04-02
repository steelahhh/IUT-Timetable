package com.alefimenko.iuttimetable.schedule.data.model

import android.view.View
import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.schedule.R
import com.alefimenko.iuttimetable.schedule.databinding.ItemSimpleTextBinding
import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.KlockLocale
import com.xwray.groupie.viewbinding.BindableItem

/*
 * Created by Alexander Efimenko on 2019-05-16.
 */

data class HeaderItem(
    val day: Int,
    val isToday: Boolean = false,
    val date: String = Constants.EMPTY_ENTRY
) : BindableItem<ItemSimpleTextBinding>(day.toLong()) {
    override fun getLayout() = R.layout.item_simple_text

    override fun initializeViewBinding(view: View): ItemSimpleTextBinding = ItemSimpleTextBinding.bind(view)

    override fun bind(viewHolder: ItemSimpleTextBinding, position: Int) = with(viewHolder) {
        val dayIdx = when (KlockLocale.default) {
            is KlockLocale.English -> day + 1
            else -> day
        }
        val dayText = DayOfWeek[dayIdx].localName(KlockLocale.default).capitalize()

        title.text = if (date != Constants.EMPTY_ENTRY) {
            root.context.getString(R.string.class_header_date_title, dayText, date)
        } else {
            dayText
        }
    }
}
