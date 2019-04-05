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
import com.alefimenko.iuttimetable.feature.schedule.model.GroupInfo
import com.alefimenko.iuttimetable.views.ErrorStubView
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.core.parameter.parametersOf

/*
 * Created by Alexander Efimenko on 2019-03-02.
 */

class PickGroupController(
    bundle: Bundle = Bundle()
) : BaseController<PickGroupFeature.UiEvent, PickGroupFeature.ViewModel>() {
    private var form: Int
    private var institute: InstituteUi?

    init {
        form = bundle.getInt(FORM_KEY)
        institute = bundle.getParcelable(INSTITUTE_KEY)
    }

    private val scope = getOrCreateScope(Scopes.PICK_GROUP)

    private val bindings: PickGroupBindings by inject { parametersOf(this) }

    private val recycler by bind<RecyclerView>(R.id.recycler)
    private val progressBar by bind<ProgressBar>(R.id.progress_bar)
    private val errorView by bind<ErrorStubView>(R.id.error_view)

    private val fastAdapter by bind.stuff {
        FastItemAdapter<GroupUi>().apply {
            withOnClickListener { _, _, group, _ ->
                dispatch(
                    PickGroupFeature.UiEvent.GroupClicked(
                        GroupInfo(
                            form = form,
                            group = group,
                            institute = institute ?: error("Institute cannot be null at this point")
                        )
                    )
                )
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
        loadGroups()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putParcelable(INSTITUTE_KEY, institute)
            putInt(FORM_KEY, form)
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        form = savedInstanceState[FORM_KEY] as Int
        institute = savedInstanceState[INSTITUTE_KEY] as InstituteUi
    }

    override fun acceptViewmodel(viewmodel: PickGroupFeature.ViewModel) {
        with(viewmodel) {
            progressBar.isVisible = isLoading
            recycler.isVisible = !isLoading
            errorView.apply {
                isVisible = isError
                textRes = R.string.group_loading_error
                onRetryClick = ::loadGroups
            }

            if (isError) {
                fastAdapter.clear()
            }

            if (isGroupsLoaded) {
                fastAdapter.set(groups)
            }
        }
    }

    private fun loadGroups() {
        institute?.let { inst ->
            dispatch(PickGroupFeature.UiEvent.LoadGroupsClicked(form, inst.id))
        }
    }

    companion object {
        const val TAG = "PickGroupScreen"
        const val FORM_KEY = "FORM"
        const val INSTITUTE_KEY = "INSTITUTE"

        fun newInstance(
            form: Int,
            institute: InstituteUi
        ): PickGroupController = PickGroupController(
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
