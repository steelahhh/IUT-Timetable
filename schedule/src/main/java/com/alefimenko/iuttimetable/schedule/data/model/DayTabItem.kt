package com.alefimenko.iuttimetable.schedule.data.model

import android.view.View
import com.alefimenko.iuttimetable.schedule.R
import com.alefimenko.iuttimetable.schedule.databinding.ItemDayTabBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder
import com.xwray.groupie.GroupieViewHolder as BasicGroupieViewHolder

data class DayTabItem(
    val classes: List<Item<*>>
) : BindableItem<ItemDayTabBinding>(classes.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.item_day_tab

    override fun initializeViewBinding(view: View): ItemDayTabBinding = ItemDayTabBinding.bind(view)

    override fun createViewHolder(itemView: View): GroupieViewHolder<ItemDayTabBinding> {
        return super.createViewHolder(itemView).apply {
            binding.dayRecyclerView.adapter = GroupAdapter<BasicGroupieViewHolder>()
        }
    }

    override fun bind(viewHolder: ItemDayTabBinding, position: Int) = with(viewHolder) {
        with(dayRecyclerView.adapter as GroupAdapter<GroupieViewHolder<ItemDayTabBinding>>) {
            clear()
            if (classes.isEmpty()) add(EmptyDayItem())
            else addAll(classes)
        }
    }
}
