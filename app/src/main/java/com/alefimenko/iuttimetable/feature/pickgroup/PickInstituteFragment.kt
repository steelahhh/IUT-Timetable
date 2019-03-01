package com.alefimenko.iuttimetable.feature.pickgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseFragment
import com.alefimenko.iuttimetable.core.di.Scopes
import com.alefimenko.iuttimetable.feature.RootActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.core.parameter.parametersOf

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

class PickInstituteFragment : BaseFragment<PickGroupFeature.UiEvent, PickGroupFeature.ViewModel>() {
    private val pickInstituteButton by bind<MaterialButton>(R.id.pick_institute_button)
    private val formRadioGroup by bind<RadioGroup>(R.id.form_radio_group)
    private val nextButton by bind<FloatingActionButton>(R.id.next_button)
    private val progressBar by bind<ProgressBar>(R.id.progress_bar)

    private var dialog: MaterialDialog? = null

    private val scope = getOrCreateScope(Scopes.PICK_GROUP)

    private val bindings: PickGroupBindings by inject { parametersOf(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings.setup(this)
        bindScope(scope)
        dialog = MaterialDialog(requireContext()).apply {
            title(text = "Выберите институт")
        }
        return inflater.inflate(R.layout.fragment_pick_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        formRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.edu_form_ochny -> dispatch(PickGroupFeature.UiEvent.FormClicked(0))
                R.id.edu_form_zaochny -> dispatch(PickGroupFeature.UiEvent.FormClicked(1))
            }
        }

        dispatch(PickGroupFeature.UiEvent.LoadInstitutesClicked)

        nextButton.setOnClickListener {
            dispatch(PickGroupFeature.UiEvent.NextButtonClicked)
            (requireActivity() as RootActivity).recreate()
        }
    }

    override fun onPause() {
        super.onPause()
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.dismiss()
        dialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
    }

    override fun accept(viewmodel: PickGroupFeature.ViewModel) {
        with(viewmodel) {
            try {
                progressBar.isVisible = isLoading
                if (institutes.isNotEmpty()) {
                    pickInstituteButton.setOnClickListener {
                        val selected = institutes.indexOf(institute)
                        dialog?.listItemsSingleChoice(
                            items = institutes.map { it.label },
                            initialSelection = selected
                        ) { dialog, index, _ ->
                            dispatch(PickGroupFeature.UiEvent.InstituteClicked(institutes[index]))
                            dialog.dismiss()
                        }
                        dialog?.show()
                    }
                }
                if (institute != null && form != -1) {
                    pickInstituteButton.apply {
                        icon = null
                        text = String.format(
                            getString(R.string.selected_institute),
                            institute.label
                        )
                    }
                    nextButton.show()
                }
            } catch (e: Exception) {
            }
        }
    }

    companion object {
        fun newInstance() = PickInstituteFragment()
    }
}
