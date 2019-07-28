package com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.base.KotlinView
import com.alefimenko.iuttimetable.extension.changeEnabled
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteFeature.Event
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteFeature.Model
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import kotlinx.android.synthetic.main.fragment_pick_institute.*

/*
 * Created by Alexander Efimenko on 2019-05-10.
 */

class PickInstituteView(
    inflater: LayoutInflater,
    container: ViewGroup,
    private val isFromSchedule: Boolean
) : KotlinView(R.layout.fragment_pick_institute, inflater, container), Connectable<Model, Event> {
    private var dialog: MaterialDialog? = null

    init {
        dialog = MaterialDialog(containerView.context).apply {
            title(R.string.pick_institute)
        }
    }

    override fun connect(output: Consumer<Event>): Connection<Model> {
        nextButton.setOnClickListener {
            output.accept(Event.NextButtonClicked)
        }

        pickGroupBackButton.setOnClickListener {
            output.accept(Event.BackClicked)
        }

        pickGroupBackButton.isGone = !isFromSchedule

        formRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.edu_form_ochny -> output.accept(Event.FormClicked(0))
                R.id.edu_form_zaochny -> output.accept(Event.FormClicked(1))
            }
        }

        return object : Connection<Model> {
            override fun accept(value: Model) {
                render(value, output)
            }

            override fun dispose() {
                tearDown()
                dialog = dialog?.listItemsSingleChoice(items = listOf(), initialSelection = 0, selection = null)
                dialog = null
            }
        }
    }

    private fun render(
        model: Model,
        output: Consumer<Event>
    ) = with(model) {
        progressBar.isVisible = isLoading
        pickInstituteButton.isEnabled = !isLoading
        errorView.apply {
            isVisible = isError
            textRes = R.string.institute_loading_error
            onRetryClick = { output.accept(Event.LoadInstitutes) }
        }

        if (institute != null) {
            pickInstituteButton.apply {
                icon = null
                text = String.format(
                    context.getString(R.string.selected_institute),
                    institute.label
                )
            }
            nextButton.show()
        }

        if (institutes.isNotEmpty()) {
            pickInstituteButton.setOnClickListener {
                val selected = institutes.indexOf(institute)
                dialog?.listItemsSingleChoice(
                    items = institutes.map { it.label },
                    initialSelection = selected
                ) { dialog, index, _ ->
                    output.accept(Event.InstituteClicked(institutes[index]))
                    dialog.dismiss()
                }
                dialog?.show()
            }
        }

        formRadioGroup.changeEnabled(enabled = !isLoading)
    }
}
