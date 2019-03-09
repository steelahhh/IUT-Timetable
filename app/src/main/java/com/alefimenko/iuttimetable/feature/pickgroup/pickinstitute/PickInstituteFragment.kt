package com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseController
import com.alefimenko.iuttimetable.core.di.Scopes
import com.alefimenko.iuttimetable.feature.pickgroup.pickinstitute.PickInstituteFeature.UiEvent
import com.alefimenko.iuttimetable.util.enableAll
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.core.parameter.parametersOf

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

class PickInstituteFragment : BaseController<UiEvent, PickInstituteFeature.ViewModel>() {
    private val pickInstituteButton by bind<MaterialButton>(R.id.pick_institute_button)
    private val formRadioGroup by bind<RadioGroup>(R.id.form_radio_group)
    private val nextButton by bind<FloatingActionButton>(R.id.next_button)
    private val progressBar by bind<ProgressBar>(R.id.progress_bar)

    private var dialog: MaterialDialog? = null

    private val scope = getOrCreateScope(Scopes.PICK_GROUP)

    private val bindings: PickInstituteBindings by inject { parametersOf(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        bindScope(scope)
        bindings.setup(this)
        return inflater.inflate(R.layout.fragment_pick_institute, container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        dialog = MaterialDialog(view.context).apply {
            title(text = "Выберите институт")
        }
        formRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.edu_form_ochny -> dispatch(UiEvent.FormClicked(0))
                R.id.edu_form_zaochny -> dispatch(UiEvent.FormClicked(1))
            }
        }
        dispatch(UiEvent.LoadInstitutesClicked)

        nextButton.setOnClickListener {
            dispatch(UiEvent.NextButtonClicked)
        }
    }

    override fun accept(viewmodel: PickInstituteFeature.ViewModel) {
        with(viewmodel) {
            try {
                progressBar.isVisible = isLoading
                pickInstituteButton.isEnabled = !isLoading

                if (isInstitutesLoaded) {
                    pickInstituteButton.setOnClickListener {
                        val selected = institutes.indexOf(institute)
                        dialog?.listItemsSingleChoice(
                            items = institutes.map { it.label },
                            initialSelection = selected
                        ) { dialog, index, _ ->
                            dispatch(UiEvent.InstituteClicked(institutes[index]))
                            dialog.dismiss()
                        }
                        dialog?.show()
                    }
                }

                if (isInstitutePicked) {
                    institute ?: return
                    pickInstituteButton.apply {
                        icon = null
                        text = String.format(
                            view?.context?.getString(R.string.selected_institute) ?: "",
                            institute.label
                        )
                    }
                    nextButton.show()
                }

                if (!isLoading) {
                    formRadioGroup.enableAll()
                }
            } catch (e: Exception) {
            }
        }
    }
}
