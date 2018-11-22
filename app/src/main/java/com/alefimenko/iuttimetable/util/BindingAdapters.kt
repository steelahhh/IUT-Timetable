package com.alefimenko.iuttimetable.util

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

/*
 * Created by Alexander Efimenko on 21/11/18.
 */

@BindingAdapter("app:setText")
fun setText(editText: TextInputEditText, text: String?) {
    editText.setText(text)
}