package com.alefimenko.iuttimetable.data

import android.os.Parcel
import android.os.Parcelable
import com.alefimenko.iuttimetable.pick_group.R
import com.xwray.groupie.Item as GroupieItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_list_group.*

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

data class GroupItem(
    val id: Int,
    val label: String
) : Item(id.toLong()), Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString() ?: "")

    override fun getLayout(): Int = R.layout.item_list_group

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder) {
        group_title.text = label
    }

    override fun isSameAs(other: GroupieItem<*>): Boolean = when (other) {
        is GroupItem -> this == other
        else -> other.isSameAs(other)
    }

    override fun hasSameContentAs(other: GroupieItem<*>): Boolean = when (other) {
        is GroupItem -> label == other.label
        else -> other.isSameAs(other)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(label)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroupItem> {
        override fun createFromParcel(parcel: Parcel): GroupItem {
            return GroupItem(parcel)
        }

        override fun newArray(size: Int): Array<GroupItem?> {
            return arrayOfNulls(size)
        }
    }
}
