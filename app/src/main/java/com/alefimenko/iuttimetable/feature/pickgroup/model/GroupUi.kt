package com.alefimenko.iuttimetable.feature.pickgroup.model

import android.view.View
import android.widget.TextView
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.model.GroupResponse
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

data class GroupUi(
    val id: Int,
    val label: String
) : AbstractItem<GroupUi, GroupUi.VH>() {
    override fun getType(): Int = R.id.item_group_id
    override fun getViewHolder(v: View) = VH(v)
    override fun getLayoutRes() = R.layout.item_group

    class VH(view: View) : FastAdapter.ViewHolder<GroupUi>(view) {
        private val title = itemView.findViewById<TextView>(R.id.group_title)

        override fun bindView(item: GroupUi, payloads: MutableList<Any>) {
            title.text = item.label
        }

        override fun unbindView(item: GroupUi) {
            title.text = null
        }
    }

    companion object {
        fun fromResponse(response: GroupResponse) = InstituteUi(response.id, response.name)
    }
}
