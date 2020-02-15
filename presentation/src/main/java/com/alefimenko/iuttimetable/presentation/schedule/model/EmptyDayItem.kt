package com.alefimenko.iuttimetable.presentation.schedule.model

import android.graphics.Color
import com.alefimenko.iuttimetable.presentation.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_simple_text.view.*

/*
 * Created by Alexander Efimenko on 2019-05-17.
 */

class EmptyDayItem : Item() {
    override fun getLayout() = R.layout.item_empty_day

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder.itemView) {
        setBackgroundColor(Color.TRANSPARENT)
        title.text = context.getString(R.string.empty_day)
    }
}
