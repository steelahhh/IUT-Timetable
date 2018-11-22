package com.alefimenko.iuttimetable.feature.pickgroup.model

import android.view.View
import androidx.databinding.DataBindingUtil
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.databinding.ItemGroupBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

data class GroupItem(
    val id: Int,
    val label: String
) : AbstractItem<GroupItem, GroupItem.VH>() {
    override fun getType(): Int = R.id.item_group_id
    override fun getViewHolder(v: View) = VH(v)
    override fun getLayoutRes() = R.layout.item_group

    class VH(view: View): FastAdapter.ViewHolder<GroupItem>(view) {

        var binding: ItemGroupBinding? = DataBindingUtil.bind(view)

        override fun bindView(item: GroupItem, payloads: MutableList<Any>) {
            binding?.group = item
        }

        override fun unbindView(item: GroupItem) {

        }

    }
}