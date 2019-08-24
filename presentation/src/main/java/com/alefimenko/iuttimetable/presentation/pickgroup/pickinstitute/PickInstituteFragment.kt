package com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.alefimenko.iuttimetable.base.BaseFragment
import com.alefimenko.iuttimetable.extension.changeEnabled
import com.alefimenko.iuttimetable.presentation.R
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.screen_pick_institute.*

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

class PickInstituteFragment : BaseFragment() {

    private val vm by fragmentViewModel(PickInstituteViewModel::class)
    private val args by args<Args>()

    private var dialog: MaterialDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.screen_pick_institute, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = MaterialDialog(requireContext()).apply {
            title(R.string.pick_institute)
        }
        pickGroupBackButton.isGone = !args.isFromSchedule

        formRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.edu_form_ochny -> vm.selectForm(0)
                R.id.edu_form_zaochny -> vm.selectForm(1)
            }
        }

        nextButton.setOnClickListener { vm.goToGroups() }
    }

    override fun invalidate() = withState(vm, ::render)

    private fun render(state: PickInstituteState) = with(state) {
        progressBar.isVisible = isLoading
        pickInstituteButton.isEnabled = !isLoading
        errorView.apply {
            isGone = !isError
            textRes = R.string.institute_loading_error
            onRetryClick = { vm.loadInstitutes() }
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
                    vm.selectInstitute(institutes[index])
                    dialog.dismiss()
                }
                dialog?.show()
            }
        }

        formRadioGroup.changeEnabled(enabled = !isLoading)
    }

    companion object {
        fun newInstance(args: Args): PickInstituteFragment = PickInstituteFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MvRx.KEY_ARG, args)
            }
        }
    }

    @Parcelize
    data class Args(
        val isFromSchedule: Boolean
    ) : Parcelable
}
