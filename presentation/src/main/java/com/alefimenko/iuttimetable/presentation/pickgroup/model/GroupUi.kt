package com.alefimenko.iuttimetable.presentation.pickgroup.model

import android.os.Parcelable
import android.view.View
import android.widget.TextView
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.data.local.model.GroupEntity
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
) : AbstractItem<GroupUi.VH>(), Parcelable {
    override val type get(): Int = R.id.item_group_id
    override fun getViewHolder(v: View) = VH(v)
    override val layoutRes get() = R.layout.item_group

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

fun GroupEntity.toGroupUi() = GroupUi(id, name)
