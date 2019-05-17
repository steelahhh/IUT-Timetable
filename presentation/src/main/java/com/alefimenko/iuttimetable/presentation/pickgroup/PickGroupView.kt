package com.alefimenko.iuttimetable.presentation.pickgroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.alefimenko.iuttimetable.base.KotlinView
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.Event
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.Model
import com.alefimenko.iuttimetable.presentation.pickgroup.model.GroupUi
import com.alefimenko.iuttimetable.presentation.schedule.model.GroupInfo
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ItemFilterListener
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import kotlinx.android.synthetic.main.fragment_pick_group.*

/*
 * Created by Alexander Efimenko on 2019-05-10.
 */

class PickGroupView(
    inflater: LayoutInflater,
    container: ViewGroup
) : KotlinView(R.layout.fragment_pick_group, inflater, container), Connectable<Model, Event> {

    private val fastAdapter = FastItemAdapter<GroupUi>()

    override fun connect(output: Consumer<Event>): Connection<Model> {
        setupViews()
        return object : Connection<Model> {
            override fun accept(value: Model) {
                fastAdapter.withOnClickListener { _, _, group, _ ->
                    output.accept(
                        PickGroupFeature.Event.GroupSelected(
                            GroupInfo(
                                form = value.form,
                                group = group,
                                institute = value.institute!!
                            )
                        )
                    )
                    false
                }
                render(value, output)
            }

            override fun dispose() = tearDown()
        }
    }

    private fun setupViews() {
        fastAdapter.apply {
            itemFilter.withItemFilterListener(object : ItemFilterListener<GroupUi> {
                override fun onReset() {
                    post {
                        errorView.isVisible = false
                    }
                }

                override fun itemsFiltered(constraint: CharSequence?, results: MutableList<GroupUi>?) {
                    post {
                        errorView.apply {
                            isVisible = results?.isEmpty() ?: false
                            textRes = R.string.search_error
                            retryVisible = false
                        }
                    }
                }
            })

            itemFilter.withFilterPredicate { item, constraint ->
                filterGroups(item, constraint)
            }
        }

        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fastAdapter
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true.also {
                fastAdapter.filter(query)
            }

            override fun onQueryTextChange(newText: String?) = true.also {
                fastAdapter.filter(newText)
            }
        })
    }

    private fun filterGroups(item: GroupUi, constraint: CharSequence?): Boolean {
        constraint ?: return false
        val query = constraint.toString().toLowerCase()
        val label = item.label.toLowerCase().replace(" ", "")
        val digits = query.replace("[a-zA-Zа-яА-Я]+".toRegex(), "").trim()
        val name = query.replace("\\d+".toRegex(), "").trim()
        return label.contains(query) || (label.contains(digits) && label.contains(name))
    }

    private fun render(model: Model, output: Consumer<Event>) = with(model) {
        progressBar.isVisible = isLoading
        recycler.isVisible = !isLoading
        errorView.apply {
            isVisible = isError
            retryVisible = true
            textRes = R.string.group_loading_error
            onRetryClick = { output.accept(Event.LoadGroups) }
        }

        if (isError) fastAdapter.clear()

        if (groups.isNotEmpty()) {
            fastAdapter.set(groups)
            if (searchView.query.isNotEmpty()) fastAdapter.filter(searchView.query)
        }
        toolbar.changeMenuColors()
    }
}
