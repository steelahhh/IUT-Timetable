package com.alefimenko.iuttimetable.settings

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.alefimenko.iuttimetable.extension.activity
import com.alefimenko.iuttimetable.settings.SettingsView.Event
import com.alefimenko.iuttimetable.settings.SettingsView.ViewModel
import com.alefimenko.iuttimetable.settings.data.SettingsItem
import com.alefimenko.iuttimetable.settings.data.SettingsItemKey
import com.alefimenko.iuttimetable.settings.databinding.RibSettingsBinding
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.jakewharton.rxrelay2.PublishRelay
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface SettingsView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    fun onThemeClick()
    fun onAboutClick()
    fun showUpdateDialog(updated: Boolean)
    fun showErrorDialog()

    sealed class Event {
        object OnBackClick : Event()
        data class SettingsItemClicked(val key: SettingsItemKey) : Event()
    }

    data class ViewModel(
        val items: List<SettingsItem> = emptyList()
    )

    interface Factory : ViewFactory<Nothing?, SettingsView>
}

class SettingsViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : SettingsView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_settings
    ) : SettingsView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> SettingsView = {
            SettingsViewImpl(inflate(it, layoutRes))
        }
    }

    private val settingsAdapter = GroupAdapter<GroupieViewHolder>()

    init {
        val binding = RibSettingsBinding.bind(androidView)
        binding.settingsBackButton.setOnClickListener {
            events.accept(Event.OnBackClick)
        }
        binding.settingsRecycler.apply {
            adapter = settingsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        settingsAdapter.setOnItemClickListener { item, _ ->
            events.accept(Event.SettingsItemClicked((item as SettingsItem).key))
        }
    }

    override fun accept(vm: ViewModel) {
        settingsAdapter.clear()
        settingsAdapter.addAll(vm.items)
    }

    override fun onThemeClick() {
        androidView.context.activity()?.recreate()
    }

    override fun onAboutClick() {
        MaterialDialog(androidView.context).show {
            title(text = androidView.context.getString(R.string.about_title, "3.1.1"))
            message(
                res = R.string.about_body
            ) {
                html()
                lineSpacing(1.4F)
            }
            positiveButton(res = R.string.common_ok)
        }
    }

    override fun showUpdateDialog(updated: Boolean) {
        MaterialDialog(androidView.context).show {
            title(text = "Обновление расписания")
            message(text = if (updated) "Расписание успешно обновлено" else "Расписание в обовлении не нуждается")
            positiveButton(res = R.string.common_ok)
        }
    }

    override fun showErrorDialog() {
        MaterialDialog(androidView.context).show {
            title(text = "Ошибка")
            message(text = "Произошла ошибка при обновлении расписания")
            positiveButton(res = R.string.common_ok)
        }
    }
}
