package com.alefimenko.iuttimetable.feature.pickgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseFragment
import com.alefimenko.iuttimetable.feature.RootActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.get

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

class PickInstituteFragment : BaseFragment() {

    private val pickInstituteButton by bind<MaterialButton>(R.id.pick_institute_button)
    private val formRadioGroup by bind<RadioGroup>(R.id.form_radio_group)
    private val nextButton by bind<FloatingActionButton>(R.id.next_button)
    private val progressBar by bind<ProgressBar>(R.id.progress_bar)

    private lateinit var vm: PickGroupViewModel

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_BUNDLE, vm.observableState.value)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_pick_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initialState = savedInstanceState?.getParcelable<State>(STATE_BUNDLE)

        vm = ViewModelProviders.of(
            this,
            PickGroupViewModelFactory(initialState, get())
        )[PickGroupViewModel::class.java]

        vm.dispatch(Action.LoadInstitutes)

        formRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.edu_form_ochny   -> vm.dispatch(Action.FormClicked(0))
                R.id.edu_form_zaochny -> vm.dispatch(Action.FormClicked(1))
            }
        }

        vm.observableState.observe(this, Observer { state ->
            state?.let {
                renderState(it)
            }
        })

        nextButton.setOnClickListener {
            vm.repository.updateTheme()
            (requireActivity() as RootActivity).recreate()
        }
    }

    private fun renderState(state: State) {
        with(state) {
            when {
                isLoading                       -> progressBar.isVisible = true
                isError && institutes.isEmpty() -> {
                    progressBar.isVisible = false
                    Toast.makeText(requireContext(), "Ошибочка", Toast.LENGTH_LONG).show()
                }
                else                            -> {
                    progressBar.isVisible = false
                    pickInstituteButton.setOnClickListener {
                        val selected = institutes.indexOf(institute)
                        MaterialDialog(requireContext()).show {
                            title(text = "Выберите институт")
                            positiveButton(text = "OK")

                            listItemsSingleChoice(
                                items = institutes.map { it.label },
                                initialSelection = selected
                            ) { _, index, _ ->
                                vm.dispatch(Action.InstituteClicked(institutes[index]))
                            }
                        }
                    }
                    if (institute != null && form != -1) {
                        pickInstituteButton.text = "Институт: ${institute.label}"
                        nextButton.show()
                    }
                }
            }
        }
    }

    companion object {
        const val STATE_BUNDLE = "BUNDLE_KEY"

        fun newInstance() = PickInstituteFragment()
    }
}
