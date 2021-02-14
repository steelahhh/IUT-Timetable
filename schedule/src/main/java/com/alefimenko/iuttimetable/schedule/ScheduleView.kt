package com.alefimenko.iuttimetable.schedule

import android.view.View
import android.widget.PopupMenu
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.alefimenko.iuttimetable.data.remote.model.Schedule
import com.alefimenko.iuttimetable.schedule.ScheduleView.Event
import com.alefimenko.iuttimetable.schedule.ScheduleView.ViewModel
import com.alefimenko.iuttimetable.schedule.data.model.ClassItem
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface ScheduleView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    val events: PublishRelay<Event>

    fun openWeekPickerDialog(weeks: List<String>, selectedWeek: Int) {
        MaterialDialog(androidView.context).show {
            title(res = R.string.pick_week_dialog_title)
            listItemsSingleChoice(
                items = weeks,
                initialSelection = selectedWeek,
                selection = { _, index, _ ->
                    events.accept(Event.SwitchToWeek(index))
                }
            )
        }
    }

    fun openPopupMenu(
        classItem: ClassItem,
        view: View,
        classIndex: Int,
        dayIndex: Int,
        weekIndex: Int
    ) = PopupMenu(view.context, view).run {
        menuInflater.inflate(R.menu.class_entry_menu, menu)
        setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.hide_class, R.id.restore_class -> {
                    events.accept(
                        Event.ChangeClassVisibility(
                            classIndex = classIndex,
                            dayIndex = dayIndex,
                            weekIndex = weekIndex
                        )
                    )
                    true
                }
                R.id.restore_class_all -> {
                    events.accept(
                        Event.RestoreAll(classItem.subject)
                    )
                    true
                }
                R.id.hide_class_all -> {
                    events.accept(
                        Event.HideAll(classItem.subject)
                    )
                    true
                }
                else -> false
            }
        }
        show()
        menu.findItem(R.id.hide_class).isVisible = !classItem.hidden
        menu.findItem(R.id.restore_class).isVisible = classItem.hidden
        menu.findItem(R.id.restore_class_all).isVisible = classItem.hidden
    }

    sealed class Event {
        data class ChangeClassVisibility(val classIndex: Int, val dayIndex: Int, val weekIndex: Int) : Event()
        data class SwitchToWeek(val weekIdx: Int) : Event()
        data class HideAll(val title: String) : Event()
        data class RestoreAll(val title: String) : Event()
        object ChangeWeek : Event()
        object OnSettingsClick : Event()
        object Retry : Event()
        object OnMenuClick : Event()
    }

    data class ViewModel(
        val isLoading: Boolean,
        val isError: Boolean,
        val schedule: Schedule? = null,
        val currentDay: Int,
        val currentWeek: Int,
        val selectedWeek: Int
    )

    interface Factory : ViewFactory<Boolean?, ScheduleView>
}
