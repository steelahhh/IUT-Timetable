package com.alefimenko.iuttimetable.pick_group

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.alefimenko.iuttimetable.data.GroupItem
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.extension.hideSoftKeyboard
import com.alefimenko.iuttimetable.pick_group.PickGroupView.Event
import com.alefimenko.iuttimetable.pick_group.PickGroupView.ViewModel
import com.alefimenko.iuttimetable.pick_group.databinding.RibPickGroupBinding
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.jakewharton.rxrelay2.PublishRelay
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PickGroupView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        data class PickGroup(val group: GroupItem) : Event()
        data class QueryChanged(val query: String) : Event()
        object GoBack : Event()
        object Retry : Event()
    }

    data class ViewModel(
        val isLoading: Boolean,
        val isError: Boolean,
        val groups: List<GroupItem>
    )

    interface Factory : ViewFactory<Nothing?, PickGroupView>
}

class PickGroupViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : PickGroupView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_pick_group
    ) : PickGroupView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> PickGroupView = {
            PickGroupViewImpl(inflate(it, layoutRes))
        }
    }

    private val groupsAdapter = GroupAdapter<GroupieViewHolder>()

    init {
        val binding = RibPickGroupBinding.bind(androidView)
        binding.toolbar.setNavigationOnClickListener { events.accept(Event.GoBack) }
        binding.recycler.run {
            layoutManager = LinearLayoutManager(context)
            adapter = groupsAdapter
        }
        binding.toolbar.changeMenuColors()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            var oldText: String? = null
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean = true.also {
                events.accept(Event.QueryChanged(newText ?: ""))
                oldText = newText
            }
        })

        binding.errorView.onRetryClick = {
            events.accept(Event.Retry)
        }
    }

    override fun accept(vm: ViewModel) = with(RibPickGroupBinding.bind(androidView)) {
        renderContent(vm)
        renderError(vm)
    }

    private fun RibPickGroupBinding.renderContent(vm: ViewModel) = with(vm) {
        progressBar.isVisible = isLoading
        recycler.isVisible = !isLoading

        if (groups.isNotEmpty()) {
            groupsAdapter.setOnItemClickListener { group, _ ->
                require(group is GroupItem)
                root.hideSoftKeyboard()
                events.accept(Event.PickGroup(group))
            }

            groupsAdapter.clear()
            groupsAdapter.updateAsync(groups)
        }
    }

    private fun RibPickGroupBinding.renderError(vm: ViewModel) = with(vm) {
        errorView.apply {
            isVisible = vm.isError
            retryVisible = true
            textRes = R.string.group_loading_error
        }

        if (groups.isEmpty() && !vm.isError && !vm.isLoading) {
            errorView.apply {
                isVisible = true
                textRes = R.string.search_error
                retryVisible = false
            }
        }

        if (isError) groupsAdapter.clear()
    }
}
