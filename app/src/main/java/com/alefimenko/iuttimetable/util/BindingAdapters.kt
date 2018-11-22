package com.alefimenko.iuttimetable.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter

/*
 * Created by Alexander Efimenko on 21/11/18.
 */

@BindingAdapter("app:setText")
fun setText(editText: TextInputEditText, text: String?) {
    editText.setText(text)
}

@BindingAdapter("app:fastAdapter")
fun <T> RecyclerView.setFastAdapter(fastAdapter: FastItemAdapter<IItem<T, FastAdapter.ViewHolder<*>>>) {
    adapter = fastAdapter
}