package com.alefimenko.iuttimetable.feature.pickgroup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.BR
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseActivity
import com.alefimenko.iuttimetable.core.data.models.GroupModel
import com.alefimenko.iuttimetable.core.di.MainComponent
import com.alefimenko.iuttimetable.databinding.ActivityPickGroupBinding
import com.alefimenko.iuttimetable.util.Constants

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class PickGroupActivity : BaseActivity<ActivityPickGroupBinding, PickGroupViewModel>(),
    PickGroupViewModel.EventListener {
    override val layoutId: Int = R.layout.activity_pick_group
    override val vmId: Int = BR.viewModel
    override val viewModelClass: Class<PickGroupViewModel> = PickGroupViewModel::class.java
    override fun viewModelFactory(): ViewModelProvider.Factory =
        MainComponent.instance.pickGroupSubComponent().viewModelFactory()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.dispatcher.bind(this, this)
    }

    override fun openInstitutePicker() {
        debouncedAction { openInstitutePickerDialog() }
    }

    private fun openInstitutePickerDialog() {
        val index = vm.selectedInstitute.value?.let {
            Constants.institutes.indexOf(it)
        } ?: -1

        MaterialDialog(this)
            .title(text = "Выберите институт")
            .positiveButton(text = "OK") {
                lastClickTime = 0
                it.dismiss()
            }
            .listItemsSingleChoice(
                items = Constants.institutes.map { it.name },
                waitForPositiveButton = true,
                initialSelection = index
            ) { _, idx, text ->
                binding.pickInstitute.text = "Институт: $text"
                vm.selectInstitute(Constants.institutes[idx])
            }
            .cancelable(false)
            .cancelOnTouchOutside(false)
            .show()
    }

    override fun changeFabVisibility(visible: Boolean) {
        with(binding.nextButton) {
            if (visible) {
                show()
            } else {
                hide()
            }
        }
    }

    override fun onNextButtonClick(institute: GroupModel, form: Int) {
        debouncedAction { Toast.makeText(this, "$institute \n $form", Toast.LENGTH_LONG).show() }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, PickGroupActivity::class.java)
    }
}