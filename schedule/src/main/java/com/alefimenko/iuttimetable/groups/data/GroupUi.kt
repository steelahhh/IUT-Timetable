package com.alefimenko.iuttimetable.groups.data

import android.os.Build
import android.view.View
import com.alefimenko.iuttimetable.Font
import com.alefimenko.iuttimetable.createTypeFace
import com.alefimenko.iuttimetable.extension.getColorCompat
import com.alefimenko.iuttimetable.extension.getDimen
import com.alefimenko.iuttimetable.schedule.R
import com.alefimenko.iuttimetable.schedule.databinding.ItemGroupBinding
import com.xwray.groupie.Item as GroupieItem
import com.xwray.groupie.viewbinding.BindableItem

/*
 * Created by Alexander Efimenko on 2019-07-30.
 */

interface OnGroupClickListener {
    fun onClick(group: GroupUi)
    fun delete(group: GroupUi)
}

data class GroupUi(
    val id: Int,
    val name: String,
    val instituteName: String,
    val semester: String,
    val isCurrent: Boolean,
    val clickListener: OnGroupClickListener? = null
) : BindableItem<ItemGroupBinding>() {
    override fun getLayout(): Int = R.layout.item_group

    override fun initializeViewBinding(view: View): ItemGroupBinding = ItemGroupBinding.bind(view)

    override fun bind(viewHolder: ItemGroupBinding, position: Int) = with(viewHolder) {
        val context = root.context
        groupEntryContainer.setOnClickListener {
            clickListener?.onClick(this@GroupUi)
        }
        groupEntryDeleteButton.setOnClickListener {
            clickListener?.delete(this@GroupUi)
        }
        val elevation = if (isCurrent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                groupEntryContainer.outlineSpotShadowColor = context.getColorCompat(R.color.blue_uni)
            context.getDimen(R.dimen.spacing_4)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                groupEntryContainer.outlineSpotShadowColor = context.getColorCompat(R.color.blue_darket)
            context.getDimen(R.dimen.spacing_2)
        }

        groupEntryContainer.cardElevation = elevation

        groupEntryName.text = name
        groupEntrySubtitle.text = semester
        if (isCurrent) groupEntryName.typeface = root.context.createTypeFace(Font.BOLD)
        else groupEntryName.typeface = root.context.createTypeFace(Font.REGULAR)
    }

    override fun isSameAs(other: GroupieItem<*>): Boolean = when (other) {
        is GroupUi -> this == other
        else -> other.isSameAs(other)
    }

    override fun hasSameContentAs(other: GroupieItem<*>): Boolean = when (other) {
        is GroupUi -> id == other.id
        else -> other.isSameAs(other)
    }
}
