package com.alefimenko.iuttimetable.schedule.data.model

import android.view.View
import com.alefimenko.iuttimetable.schedule.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_day_tab.*

data class DayTabItem(
    val classes: List<Item>
) : Item(id = classes.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.item_day_tab

    override fun createViewHolder(
        itemView: View
    ): GroupieViewHolder = super.createViewHolder(itemView).apply {
        dayRecyclerView.adapter = GroupAdapter<GroupieViewHolder>()
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder) {
        with(dayRecyclerView.adapter as GroupAdapter<GroupieViewHolder>) {
            clear()
            if (classes.isEmpty()) add(EmptyDayItem())
            else addAll(classes)
        }
    }
}
