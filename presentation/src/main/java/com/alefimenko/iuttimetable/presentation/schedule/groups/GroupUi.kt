package com.alefimenko.iuttimetable.presentation.schedule.groups

import android.os.Build
import com.alefimenko.iuttimetable.Font
import com.alefimenko.iuttimetable.createTypeFace
import com.alefimenko.iuttimetable.extension.getColorCompat
import com.alefimenko.iuttimetable.extension.getDimen
import com.alefimenko.iuttimetable.presentation.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_group.*

/*
 * Created by Alexander Efimenko on 2019-07-30.
 */

data class GroupUi(
    val id: Int,
    val name: String,
    val instituteName: String,
    val semester: String,
    val isCurrent: Boolean,
    val clickListener: OnGroupClickListener
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) = with(viewHolder) {
        val context = itemView.context
        groupEntryContainer.setOnClickListener {
            clickListener.onClick(this@GroupUi)
        }
        groupEntryDeleteButton.setOnClickListener {
            clickListener.delete(this@GroupUi)
        }
        val elevation = if (isCurrent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                groupEntryContainer.outlineSpotShadowColor = context.getColorCompat(R.color.iutColor)
            context.getDimen(R.dimen.spacing_4)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                groupEntryContainer.outlineSpotShadowColor = context.getColorCompat(R.color.backgroundDark)
            context.getDimen(R.dimen.spacing_2)
        }

        groupEntryContainer.cardElevation = elevation

        groupEntryName.text = name
        groupEntrySubtitle.text = semester
        if (isCurrent) groupEntryName.typeface = itemView.context.createTypeFace(Font.BOLD)
        else groupEntryName.typeface = itemView.context.createTypeFace(Font.REGULAR)
    }

    override fun getLayout(): Int = R.layout.item_group
}
