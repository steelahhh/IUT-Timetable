package com.alefimenko.iuttimetable.feature.pickgroup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.arch.AutoDisposable
import com.alefimenko.iuttimetable.core.arch.BasePreferencesViewModel
import com.alefimenko.iuttimetable.core.arch.EventDispatcher
import com.alefimenko.iuttimetable.core.data.local.LocalPreferences
import com.alefimenko.iuttimetable.core.data.models.GroupModel
import com.alefimenko.iuttimetable.core.data.remote.ScheduleService
import com.alefimenko.iuttimetable.util.*
import javax.inject.Inject

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class PickGroupViewModel @Inject constructor(
    val context: Context,
    preferences: LocalPreferences,
    val scheduleService: ScheduleService,
    val dispatcher: EventDispatcher<EventListener>
) : BasePreferencesViewModel(preferences) {
    private val _selectedInstitute = MutableLiveData<GroupModel>()
    private val _selectedForm = MutableLiveData<Int>().default(0)
    private val _institutes = MutableLiveData<Result<List<GroupModel>>>()
        .default(Result.success(Constants.institutes))

    init {
        dispatcher.dispatchEvent { changeFabVisibility(visible = false) }
    }

    val autoDisposable = AutoDisposable()

    val selectedInstitute: LiveData<GroupModel>
        get() = _selectedInstitute

    val selectedForm: LiveData<Int>
        get() = _selectedForm

    fun fetchInstitutes() {
        if (NetworkUtils.isNetworkAvailable(context)) {
            scheduleService.fetchInstitutes().ioMainSchedulers()
                .subscribe({
                    _institutes.postValue(Result.success(it))
                }, {
                    _institutes.postValue(Result.failure(it))
                }).addTo(autoDisposable)
        }
    }

    fun selectInstitute(institute: GroupModel) {
        _selectedInstitute.postValue(institute)
        dispatcher.dispatchEvent { changeFabVisibility(visible = true) }
    }

    fun openInstitutePicker() {
        dispatcher.dispatchEvent { openInstitutePicker() }
    }

    fun onFormChecked(checkedId: Int) {
        when (checkedId) {
            R.id.edu_form_ochny -> _selectedForm.postValue(0)
            R.id.edu_form_zaochny -> _selectedForm.postValue(1)
        }
    }

    fun onNextButtonClicked() {
        dispatcher.dispatchEvent {
            onNextButtonClick(
                selectedInstitute.value!!,
                selectedForm.value!!
            )
        }
    }

    interface EventListener {
        fun openInstitutePicker()
        fun changeFabVisibility(visible: Boolean)
        fun onNextButtonClick(institute: GroupModel, form: Int)
    }
}