package com.alefimenko.iuttimetable.presentation.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.base.KotlinView
import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.root.RootActivity
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleFeature.Event
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleFeature.Model
import com.alefimenko.iuttimetable.presentation.schedule.groups.GroupsFragment
import com.alefimenko.iuttimetable.presentation.schedule.model.ClassUi
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
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.screen_schedule.*

/*
 * Created by Alexander Efimenko on 2019-05-10.
 */

class ScheduleView(
    inflater: LayoutInflater,
    container: ViewGroup,
    private val activity: RootActivity
) : KotlinView(R.layout.screen_schedule, inflater, container) {

    private val itemAdapter = GroupAdapter<GroupieViewHolder>()
    private val insideEvents = PublishSubject.create<Event>()
    private val headerIndices = mutableListOf<Int>()

    val connector: Connectable<Model, Event> = RxConnectables.fromTransformer(::connect)

    private fun connect(models: Observable<Model>): Observable<Event> {
        setupViews()

        val compositeDisposable = CompositeDisposable()

        compositeDisposable += models.distinctUntilChanged().subscribe(::render)

        toolbar.setNavigationOnClickListener {
            val fra = GroupsFragment({ groupId ->
                insideEvents.onNext(Event.DisplaySchedule(groupId))
            }, { groupId ->
                insideEvents.onNext(Event.DeleteSchedule(groupId))
            })
            fra.show(activity.supportFragmentManager, "BottomSheetDialogFragment")
        }

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

    fun showWeeksDialog(weeks: List<String>, selectedWeek: Int) {
        MaterialDialog(containerView.context).show {
            title(res = R.string.pick_week_dialog_title)
            listItemsSingleChoice(
                items = weeks,
                initialSelection = selectedWeek,
                selection = { _, index, _ ->
                    insideEvents.onNext(Event.SwitchWeek(index))
                }
            )
        }
    }

    private fun render(model: Model) = with(model) {
        scheduleStubView.apply {
            isVisible = isError
            textRes = R.string.schedule_loading_error
            onRetryClick = { insideEvents.onNext(Event.DownloadSchedule) }
        }

        recyclerView.isGone = isLoading || isError
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
            days.forEachIndexed { dayIndex, list ->
                headerIndices.add(items.itemCount.takeIf { it != 1 } ?: 0)
                items.add(Section().apply {
                    setHeader(
                        HeaderItem(
                            dayIndex,
                            dayIndex == currentDay,
                            list.firstOrNull()?.date ?: Constants.EMPTY_ENTRY
                        )
                    )
                    if (list.isEmpty()) add(EmptyDayItem())
                    else addAll(list.mapIndexed { listIdx, classEntry ->
                        val position = when {
                            list.size == 1 -> Position.SINGLE
                            listIdx == 0 -> Position.FIRST
                            listIdx == list.size - 1 -> Position.LAST
                            else -> Position.OTHER
                        }
                        classEntry.toClassUi(
                            position,
                            onClassMenuClick = { classUi, view ->
                                openPopupMenu(classUi, view, listIdx, dayIndex, selectedWeek)
                            })
                    })
                })
            }
        }
        scheduleChangeWeekButton.isGone = isError || isLoading
        scheduleChangeWeekButton.text =
            schedule?.weeks?.get(selectedWeek)
                ?: containerView.context.getString(R.string.menu_change_week)
        itemAdapter.add(items)
        itemAdapter.notifyDataSetChanged()
        progressBar.isVisible = isLoading
    }

    private fun openPopupMenu(
        classUi: ClassUi,
        view: View,
        classIndex: Int,
        dayIndex: Int,
        weekIndex: Int
    ) {
        PopupMenu(view.context, view).apply {
            menuInflater.inflate(R.menu.class_entry_menu, menu)
            setOnMenuItemClickListener {
                insideEvents.onNext(Event.ChangeClassVisibility(classIndex, dayIndex, weekIndex))
                true
            }
            show()
            menu.findItem(R.id.hide_class).isVisible = !classUi.hidden
            menu.findItem(R.id.restore_class).isVisible = classUi.hidden
        }
    }

    private fun setupViews() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemAdapter
        }
        toolbar.apply {
            replaceMenu(R.menu.schedule_menu)
            changeMenuColors()
        }
    }
}
