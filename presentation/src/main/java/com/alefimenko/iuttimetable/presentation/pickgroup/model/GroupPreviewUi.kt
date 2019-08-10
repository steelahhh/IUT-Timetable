package com.alefimenko.iuttimetable.presentation.pickgroup.model

import android.os.Parcelable
import android.view.View
import android.widget.TextView
import com.alefimenko.iuttimetable.data.local.model.GroupEntity
import com.alefimenko.iuttimetable.data.remote.model.GroupResponse
import com.alefimenko.iuttimetable.presentation.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.parcel.Parcelize

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Parcelize
data class GroupPreviewUi(
    val id: Int,
    val label: String
) : AbstractItem<GroupPreviewUi.VH>(), Parcelable {
    override val type get(): Int = R.id.item_group_id
    override fun getViewHolder(v: View) = VH(v)
    override val layoutRes get() = R.layout.item_list_group

    class VH(view: View) : FastAdapter.ViewHolder<GroupPreviewUi>(view) {
        private val title = itemView.findViewById<TextView>(R.id.group_title)

        override fun bindView(item: GroupPreviewUi, payloads: MutableList<Any>) {
            title.text = item.label
        }

        override fun unbindView(item: GroupPreviewUi) {
            title.text = null
        }
    }
}

fun GroupResponse.toGroupUi() = GroupPreviewUi(id, name)
fun GroupResponse.toInstituteUi() = InstituteUi(id, name)
fun GroupEntity.toGroupUi() = GroupPreviewUi(id, name)
