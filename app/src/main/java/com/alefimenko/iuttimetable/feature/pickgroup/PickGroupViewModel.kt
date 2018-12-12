package com.alefimenko.iuttimetable.feature.pickgroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.arch.BasePreferencesViewModel
import com.alefimenko.iuttimetable.core.arch.EventDispatcher
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.core.data.models.GroupEntity
import com.alefimenko.iuttimetable.util.default
import javax.inject.Inject

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class PickGroupViewModel @Inject constructor(
    preferences: LocalPreferences,
    val dispatcher: EventDispatcher<EventListener>
) : BasePreferencesViewModel(preferences) {
    private val _selectedInstitute = MutableLiveData<GroupEntity>()
    private val _selectedForm = MutableLiveData<Int>().default(0)

    init {
        dispatcher.dispatchEvent { changeFabVisibility(visible = false) }
    }

    val selectedInstitute: LiveData<GroupEntity>
        get() = _selectedInstitute

    val selectedForm: LiveData<Int>
        get() = _selectedForm

    fun selectInstitute(institute: GroupEntity) {
        _selectedInstitute.postValue(institute)
        dispatcher.dispatchEvent { changeFabVisibility(visible = true) }
    }

    fun openInstitutePicker() {
        dispatcher.dispatchEvent { openInstitutePicker() }
    }

    fun onFormChecked(checkedId: Int) {
        when (checkedId) {
            R.id.eduFormOchny -> _selectedForm.postValue(0)
            R.id.eduFormZaochny -> _selectedForm.postValue(1)
        }
    }

    fun onNextButtonClicked() {
        dispatcher.dispatchEvent { onNextButtonClick(selectedInstitute.value!!, selectedForm.value!!) }
    }

    interface EventListener {
        fun openInstitutePicker()
        fun changeFabVisibility(visible: Boolean)
        fun onNextButtonClick(institute: GroupEntity, form: Int)
    }
}