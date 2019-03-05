package com.alefimenko.iuttimetable.feature.pickgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseFragment
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import kotlinx.android.synthetic.main.fragment_pick_group.*

/*
 * Created by Alexander Efimenko on 2019-03-02.
 */

class PickGroupFragment : BaseFragment<String, String>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_pick_group, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val form = arguments?.getInt(FORM_KEY)
        val institute = arguments?.getParcelable<InstituteUi>(INSTITUTE_KEY)
        val str = "$form $institute"
        text.text = str
    }

    override fun accept(t: String?) {}

    companion object {
        const val FORM_KEY = "FORM"
        const val INSTITUTE_KEY = "INSTITUTE"

        fun newInstance(
            form: Int,
            institute: InstituteUi
        ): PickGroupFragment = PickGroupFragment().apply {
            arguments = Bundle().apply {
                putInt(FORM_KEY, form)
                putParcelable(INSTITUTE_KEY, institute)
            }
        }

        fun createBundle(
            form: Int,
            institute: InstituteUi
        ) = Bundle().apply {
            putInt(FORM_KEY, form)
            putParcelable(INSTITUTE_KEY, institute)
        }
    }
}
