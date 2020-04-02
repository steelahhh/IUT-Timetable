package com.alefimenko.iuttimetable.schedule.data.model

import android.view.View
import com.alefimenko.iuttimetable.schedule.R
import com.alefimenko.iuttimetable.schedule.databinding.ItemScheduleHeaderBinding
import com.xwray.groupie.viewbinding.BindableItem

/*
 * Created by Alexander Efimenko on 2019-07-09.
 */

data class ScheduleInfoHeaderItem(
    val group: String,
    val semester: String,
    val isWeekOdd: Boolean
) : BindableItem<ItemScheduleHeaderBinding>() {
    override fun initializeViewBinding(view: View): ItemScheduleHeaderBinding = ItemScheduleHeaderBinding.bind(view)

    override fun getLayout(): Int = R.layout.item_schedule_header

    override fun bind(viewHolder: ItemScheduleHeaderBinding, position: Int) = with(viewHolder) {
        scheduleHeaderTitle.text = group
        scheduleHeaderSubtitle.text = semester
    }
}
