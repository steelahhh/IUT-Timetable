package com.alefimenko.iuttimetable.feature.pickgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.base.BaseController
import com.alefimenko.iuttimetable.core.di.Scopes
import com.alefimenko.iuttimetable.feature.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.feature.pickgroup.model.InstituteUi
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.core.parameter.parametersOf

/*
 * Created by Alexander Efimenko on 2019-03-02.
 */

@Suppress("unused")
class PickGroupFragment(
    bundle: Bundle?
) : BaseController<PickGroupFeature.UiEvent, PickGroupFeature.ViewModel>() {

    constructor() : this(null)

    private val form: Int
    private val institute: InstituteUi?

    init {
        form = bundle?.getInt(FORM_KEY) ?: 0
        institute = bundle?.getParcelable(INSTITUTE_KEY)
    }

    private val scope = getOrCreateScope(Scopes.PICK_GROUP)

    private val bindings: PickGroupBindings by inject { parametersOf(this) }

    private val recycler by bind<RecyclerView>(R.id.recycler)
    private val progressBar by bind<ProgressBar>(R.id.progress_bar)

    private val fastAdapter by bind.stuff {
        FastItemAdapter<GroupUi>().apply {
            withOnClickListener { _, _, group, _ ->
                dispatch(PickGroupFeature.UiEvent.GroupClicked(group))
                false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        bindScope(scope)
        bindings.setup(this)
        return inflater.inflate(R.layout.fragment_pick_group, container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        recycler.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = fastAdapter
        }
        if (institute != null) {
            dispatch(PickGroupFeature.UiEvent.LoadGroupsClicked(form, institute.id))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
    }

    override fun accept(viewModel: PickGroupFeature.ViewModel) {
        with(viewModel) {
            try {
                progressBar.isVisible = isLoading
                recycler.isVisible = !isLoading

                if (isGroupsLoaded) {
                    fastAdapter.set(groups)
                }
            } catch (e: Exception) {
            }
        }
    }

    companion object {
        const val FORM_KEY = "FORM"
        const val INSTITUTE_KEY = "INSTITUTE"

        fun newInstance(
            form: Int,
            institute: InstituteUi
        ): PickGroupFragment = PickGroupFragment(
            Bundle().apply {
                putInt(FORM_KEY, form)
                putParcelable(INSTITUTE_KEY, institute)
            }
        )

        fun createBundle(
            form: Int,
            institute: InstituteUi
        ) = Bundle().apply {
            putInt(FORM_KEY, form)
            putParcelable(INSTITUTE_KEY, institute)
        }
    }
}
