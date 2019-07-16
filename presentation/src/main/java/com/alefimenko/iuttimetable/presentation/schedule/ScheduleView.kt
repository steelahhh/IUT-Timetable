package com.alefimenko.iuttimetable.presentation.schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.alefimenko.iuttimetable.base.KotlinView
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleFeature.Event
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleFeature.Model
import com.alefimenko.iuttimetable.presentation.schedule.model.EmptyDayItem
import com.alefimenko.iuttimetable.presentation.schedule.model.HeaderItem
import com.alefimenko.iuttimetable.presentation.schedule.model.Position
import com.alefimenko.iuttimetable.presentation.schedule.model.ScheduleInfoHeader
import com.alefimenko.iuttimetable.presentation.schedule.model.toClassUi
import com.jakewharton.rxbinding3.appcompat.itemClicks
import com.jakewharton.rxbinding3.view.clicks
import com.spotify.mobius.Connectable
import com.spotify.mobius.rx2.RxConnectables
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_schedule.*

/*
 * Created by Alexander Efimenko on 2019-05-10.
 */

class ScheduleView(
    inflater: LayoutInflater,
    container: ViewGroup
) : KotlinView(R.layout.fragment_schedule, inflater, container) {

    private val itemAdapter = GroupAdapter<ViewHolder>()
    private val insideEvents = PublishSubject.create<Event>()
    private val headerIndices = mutableListOf<Int>()

    val connector: Connectable<Model, Event> = RxConnectables.fromTransformer(::connect)

    private fun connect(models: Observable<Model>): Observable<Event> {
        setupViews()

        val compositeDisposable = CompositeDisposable()

        compositeDisposable += models.distinctUntilChanged().subscribe(::render)

        return Observable.mergeArray<Event>(
            insideEvents.hide(),
            toolbar.itemClicks().map {
                Event.NavigateToSettings
            },
            scheduleChangeWeekButton.clicks().map {
                Event.RequestWeekChange
            }
        ).doOnDispose {
            tearDown()
            compositeDisposable.dispose()
        }
    }

    fun switchToCurrent(day: Int) {
        if (itemAdapter.itemCount != 0) {
            val smoothScroller = object : LinearSmoothScroller(containerView.context) {
                override fun getVerticalSnapPreference() = SNAP_TO_START
            }
            smoothScroller.targetPosition = headerIndices[day]
            recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun render(model: Model) = with(model) {
        recyclerView.isGone = isLoading
        itemAdapter.clear()
        val items = Section(
            ScheduleInfoHeader(
                model.schedule?.groupTitle ?: "",
                model.schedule?.semester ?: "",
                currentWeek == 0
            )
        )
        schedule?.weekSchedule?.get(selectedWeek)?.let { days ->
            headerIndices.clear()
            days.forEachIndexed { index, list ->
                headerIndices.add(items.itemCount)
                items.add(Section().apply {
                    setHeader(HeaderItem(index, index == currentDay))
                    if (list.isEmpty()) add(EmptyDayItem())
                    else addAll(list.mapIndexed { listIdx, classEntry ->
                        val position = when {
                            list.size == 1 -> Position.SINGLE
                            listIdx == 0 -> Position.FIRST
                            listIdx == list.size - 1 -> Position.LAST
                            else -> Position.OTHER
                        }
                        classEntry.toClassUi(position)
                    })
                })
            }
        }
        itemAdapter.add(items)
        progressBar.isVisible = isLoading
    }

    private fun setupViews() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemAdapter
            setHasFixedSize(true)
        }
        toolbar.apply {
            replaceMenu(R.menu.schedule_menu)
            setNavigationOnClickListener {
            }
            changeMenuColors()
        }
    }
}
