package com.alefimenko.iuttimetable.data

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.alefimenko.iuttimetable.pick_group.R
import com.alefimenko.iuttimetable.pick_group.databinding.ItemListGroupBinding
import com.xwray.groupie.Item as GroupieItem
import com.xwray.groupie.viewbinding.BindableItem

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

data class GroupItem(
    val id: Int,
    val label: String
) : BindableItem<ItemListGroupBinding>(id.toLong()), Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString() ?: "")

    override fun getLayout(): Int = R.layout.item_list_group

    override fun initializeViewBinding(view: View): ItemListGroupBinding = ItemListGroupBinding.bind(view)

    override fun bind(viewBinding: ItemListGroupBinding, position: Int) {
        viewBinding.groupTitle.text = label
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
