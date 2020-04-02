package com.alefimenko.iuttimetable.schedule.data.model

import android.graphics.Color
import android.view.View
import com.alefimenko.iuttimetable.schedule.R
import com.alefimenko.iuttimetable.schedule.databinding.ItemEmptyDayBinding
import com.xwray.groupie.viewbinding.BindableItem

/*
 * Created by Alexander Efimenko on 2019-05-17.
 */

class EmptyDayItem : BindableItem<ItemEmptyDayBinding>() {
    override fun getLayout() = R.layout.item_empty_day

    override fun initializeViewBinding(view: View): ItemEmptyDayBinding = ItemEmptyDayBinding.bind(view)

    override fun bind(viewHolder: ItemEmptyDayBinding, position: Int) = with(viewHolder) {
        root.setBackgroundColor(Color.TRANSPARENT)
        title.text = root.context.getString(R.string.empty_day)
    }
}
