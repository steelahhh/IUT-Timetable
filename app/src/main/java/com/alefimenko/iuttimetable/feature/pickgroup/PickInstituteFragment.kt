package com.alefimenko.iuttimetable.feature.pickgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseFragment
import com.alefimenko.iuttimetable.feature.RootActivity
import com.alefimenko.iuttimetable.util.Constants
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

class PickInstituteFragment : BaseFragment() {

    private val pickInstituteButton by bind<MaterialButton>(R.id.pick_institute_button)
    private val formRadioGroup by bind<RadioGroup>(R.id.form_radio_group)
    private val nextButton by bind<FloatingActionButton>(R.id.next_button)
    private val progressBar by bind<ProgressBar>(R.id.progress_bar)

    private val repository: PickGroupRepository by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_pick_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        formRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.edu_form_ochny   -> ""
                R.id.edu_form_zaochny -> ""
            }
        }
        pickInstituteButton.setOnClickListener {
            MaterialDialog(requireContext()).apply {
                title(text = "Выберите институт")
                listItemsSingleChoice(items = Constants.institutes.map { it.name }) { _, index, text ->
                    pickInstituteButton.text = "Институт: $text"
                }
                positiveButton(text = "OK")
            }.show()
        }
        nextButton.setOnClickListener {
            repository.updateTheme()
            (requireActivity() as RootActivity).recreate()
        }
    }

    companion object {
        fun newInstance() = PickInstituteFragment()
    }
}
