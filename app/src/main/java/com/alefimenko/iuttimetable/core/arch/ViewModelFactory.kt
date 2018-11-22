package com.alefimenko.iuttimetable.core.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val initBlock: () -> ViewModel): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return initBlock() as T
    }
}