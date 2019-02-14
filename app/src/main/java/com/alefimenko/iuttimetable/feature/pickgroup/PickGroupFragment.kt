package com.alefimenko.iuttimetable.feature.pickgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

class PickGroupFragment : BaseFragment() {

    private val pickInstituteButton by bind<MaterialButton>(R.id.pick_institute_button)
    private val formRadioGroup by bind<RadioGroup>(R.id.form_radio_group)
    private val nextButton by bind<FloatingActionButton>(R.id.next_button)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_pick_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance() = PickGroupFragment()
    }
}
