package com.alefimenko.iuttimetable.presentation.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.alefimenko.iuttimetable.base.KotlinView
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Event
import com.alefimenko.iuttimetable.presentation.settings.SettingsFeature.Model
import com.alefimenko.iuttimetable.presentation.settings.model.SettingsItem
import com.alefimenko.iuttimetable.presentation.settings.model.SettingsItemKey
import com.jakewharton.rxbinding3.view.clicks
import com.spotify.mobius.Connectable
import com.spotify.mobius.rx2.RxConnectables
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.screen_settings.*

/*
 * Created by Alexander Efimenko on 2019-07-08.
 */

class SettingsView(
    inflater: LayoutInflater,
    container: ViewGroup
) : KotlinView(R.layout.screen_settings, inflater, container) {

    private val settingsAdapter = GroupAdapter<ViewHolder>()

    private val insideEvents = PublishSubject.create<Event>()

    val connector: Connectable<Model, Event> = RxConnectables.fromTransformer(::connect)

    private fun connect(models: Observable<Model>): Observable<Event> {
        setupViews()

        val cd = CompositeDisposable()

        settingsAdapter.setOnItemClickListener { item, _ ->
            insideEvents.onNext(Event.SettingsItemClicked((item as SettingsItem).key))
        }

        cd += models.distinctUntilChanged().subscribe(::render)

        return Observable.mergeArray(
            insideEvents.hide(),
            settingsBackButton.clicks().map { Event.BackClicked }
        ).doOnDispose {
            tearDown()
            cd.dispose()
        }
    }

    private fun setupViews() {
        settingsRecycler.apply {
            adapter = settingsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
    }

    private fun render(model: Model) {
        settingsAdapter.clear()
        settingsAdapter.addAll(model.buildSection())
    }

    private fun Model.buildSection() = listOf(
        // SettingsItem(
        //     key = SettingsItemKey.Language,
        //     titleRes = R.string.settings_language_title,
        //     switcherVisible = false
        // ),
        SettingsItem(
            key = SettingsItemKey.DarkTheme,
            titleRes = R.string.settings_theme_title,
            isChecked = isDarkTheme,
            switcherVisible = true
        ),
        SettingsItem(
            key = SettingsItemKey.RelevantSchedule,
            titleRes = R.string.settings_relevant_schedule_title,
            subtitleRes = R.string.settings_relevant_schedule_subtitle,
            isChecked = alwaysRelevantSchedule,
            switcherVisible = true
        ),
        SettingsItem(
            key = SettingsItemKey.WeekCountDown,
            titleRes = R.string.settings_week_countdown_title,
            subtitleRes = R.string.settings_week_countdown_subtitle,
            iconRes = R.drawable.ic_pick_date,
            isChecked = changeWeekCountdown,
            switcherVisible = true
        ),
        SettingsItem(
            key = SettingsItemKey.Feedback,
            titleRes = R.string.settings_feedback_title,
            switcherVisible = false,
            iconRes = R.drawable.ic_mail_outline
        ),
        SettingsItem(
            key = SettingsItemKey.About,
            titleRes = R.string.settings_about_title,
            switcherVisible = false,
            iconRes = R.drawable.ic_info_outline
        )
    )
}
