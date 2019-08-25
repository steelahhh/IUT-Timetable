package com.alefimenko.iuttimetable.presentation.pickgroup

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.alefimenko.iuttimetable.base.BaseFragment
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.pickgroup.model.GroupPreviewUi
import com.alefimenko.iuttimetable.presentation.pickgroup.model.InstituteUi
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ItemFilterListener
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.screen_pick_group.*

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

class PickGroupFragment : BaseFragment() {

    private val vm by fragmentViewModel(PickGroupViewModel::class)
    private val args by args<Args>()

    private val fastAdapter = FastItemAdapter<GroupPreviewUi>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.screen_pick_group, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun invalidate() = withState(vm, ::render)

    override fun onBackPressed() = vm.exit()

    private fun render(state: PickGroupState) = with(state) {
        progressBar.isVisible = isLoading
        recycler.isVisible = !isLoading
        errorView.apply {
            isVisible = isError
            retryVisible = true
            textRes = R.string.group_loading_error
            onRetryClick = { vm.loadGroups() }
        }

        if (isError) fastAdapter.clear()

        if (groups.isNotEmpty()) {
            fastAdapter.set(groups)
            if (searchView.query.isNotEmpty()) fastAdapter.filter(searchView.query)
        }
        toolbar.changeMenuColors()
    }

    private fun setupViews() {
        fastAdapter.apply {
            itemFilter.itemFilterListener = object : ItemFilterListener<GroupPreviewUi> {
                override fun onReset() {
                    post {
                        errorView.isVisible = false
                    }
                }

                override fun itemsFiltered(constraint: CharSequence?, results: List<GroupPreviewUi>?) {
                    post {
                        errorView.apply {
                            isVisible = results?.isEmpty() ?: false
                            textRes = R.string.search_error
                            retryVisible = false
                        }
                    }
                }
            }

            itemFilter.filterPredicate = { item, constraint ->
                vm.filterGroups(item, constraint)
            }

            onClickListener = { _, _, group, _ ->
                vm.goToSchedule(group)
                false
            }
        }

        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fastAdapter
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true.also {
                fastAdapter.filter(query.orEmpty() as CharSequence)
            }

            override fun onQueryTextChange(newText: String?) = true.also {
                fastAdapter.filter(newText.orEmpty() as CharSequence)
            }
        })
    }

    companion object {
        fun newInstance(args: Args) = PickGroupFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MvRx.KEY_ARG, args)
            }
        }
    }

    @Parcelize
    data class Args(
        val form: Int,
        val institute: InstituteUi
    ) : Parcelable
}
