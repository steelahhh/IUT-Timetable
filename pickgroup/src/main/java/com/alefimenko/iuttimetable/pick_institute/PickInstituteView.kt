package com.alefimenko.iuttimetable.pick_institute

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.data.Institute
import com.alefimenko.iuttimetable.extension.changeEnabled
import com.alefimenko.iuttimetable.pick_group.R
import com.alefimenko.iuttimetable.pick_institute.PickInstituteView.Event
import com.alefimenko.iuttimetable.pick_institute.PickInstituteView.ViewModel
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.rib_pick_institute.view.*

interface PickInstituteView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object GoBack : Event()
        object Retry : Event()
        object NextClicked : Event()
        data class FormPicked(val form: Int) : Event()
        data class InstituteClicked(val institute: Institute) : Event()
    }

    data class ViewModel(
        val isLoading: Boolean,
        val isError: Boolean,
        val institutes: List<Institute>,
        val institute: Institute? = null
    )

    interface Factory : ViewFactory<Boolean, PickInstituteView>
}

class PickInstituteViewImpl private constructor(
    override val androidView: ViewGroup,
    private val isRoot: Boolean,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : PickInstituteView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_pick_institute
    ) : PickInstituteView.Factory {
        override fun invoke(deps: Boolean): (ViewGroup) -> PickInstituteView = {
            PickInstituteViewImpl(
                inflate(it, layoutRes),
                deps
            )
        }
    }

    // TODO replace this with overlay RIB
    private var dialog: MaterialDialog? = null

    init {
        androidView.pickGroupBackButton.run {
            isGone = isRoot
            setOnClickListener { events.accept(Event.GoBack) }
        }

        dialog = MaterialDialog(androidView.context).apply {
            title(R.string.pick_institute)
        }
    }

    override fun accept(vm: ViewModel) = with(androidView) {
        progressBar.isGone = !vm.isLoading
        pickInstituteButton.isEnabled = !vm.isLoading
        formRadioGroup.changeEnabled(vm.institutes.isNotEmpty())

        errorView.apply {
            isVisible = vm.isError
            textRes = R.string.institute_loading_error
            onRetryClick = { events.accept(Event.Retry) }
        }

        formRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            events.accept(
                if (checkedId == R.id.edu_form_ochny) Event.FormPicked(0)
                else Event.FormPicked(1)
            )
        }

        nextButton.setOnClickListener {
            events.accept(Event.NextClicked)
        }

        if (vm.institutes.isNotEmpty()) {
            pickInstituteButton.setOnClickListener {
                val selected = vm.institutes.indexOf(vm.institute)
                dialog?.listItemsSingleChoice(
                    items = vm.institutes.map { it.name },
                    initialSelection = selected
                ) { dialog, index, _ ->
                    events.accept(Event.InstituteClicked(vm.institutes[index]))
                    dialog.dismiss()
                }
                dialog?.show()
            }
        }

        if (vm.institute != null) {
            pickInstituteButton.apply {
                icon = null
                text = String.format(
                    context.getString(R.string.selected_institute),
                    vm.institute.name
                )
            }
            nextButton.show()
        }
    }
}
