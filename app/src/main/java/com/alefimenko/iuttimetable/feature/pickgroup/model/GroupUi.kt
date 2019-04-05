package com.alefimenko.iuttimetable.feature.pickgroup.model

import android.os.Parcelable
import android.view.View
import android.widget.TextView
import com.alefimenko.iuttimetable.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Parcelize
data class GroupUi(
    val id: Int,
    val label: String
) : AbstractItem<GroupUi, GroupUi.VH>(), Parcelable {
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
}
