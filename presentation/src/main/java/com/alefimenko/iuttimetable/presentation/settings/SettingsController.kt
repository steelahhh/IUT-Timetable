package com.alefimenko.iuttimetable.presentation.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.alefimenko.iuttimetable.base.BaseController
import com.alefimenko.iuttimetable.extension.requireActivity
import com.alefimenko.iuttimetable.extension.requireContext
import com.alefimenko.iuttimetable.presentation.BuildConfig.VERSION_NAME
import com.alefimenko.iuttimetable.presentation.R
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid.controller
import com.spotify.mobius.rx2.RxMobius
import org.koin.android.ext.android.get

/*
 * Created by Alexander Efimenko on 2019-07-08.
 */

class SettingsController : BaseController(), SettingsViewContract {

    private var settingsView: SettingsView? = null

    private var dialog: MaterialDialog? = null

    private val controller: MobiusLoop.Controller<SettingsFeature.Model, SettingsFeature.Event> =
        controller(
            RxMobius.loop(
                SettingsFeature.SettingsUpdater,
                SettingsFeature.SettingsEffectHandler(
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    this
                ).create()
            ).init(SettingsFeature.SettingsInitializer).logger(AndroidLogger.tag("SETTINGS")),
            SettingsFeature.Model()
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View = SettingsView(inflater, container).apply {
        controller.connect(connector)
        controller.start()
        settingsView = this
    }.containerView

    override fun onDestroyView(view: View) {
        dialog?.dismiss()
        dialog = null
        controller.stop()
        controller.disconnect()
        super.onDestroyView(view)
    }

    override fun onThemeClick() {
        requireActivity().recreate()
    }

    override fun onAboutClick() {
        MaterialDialog(requireContext()).show {
            title(text = requireContext().getString(R.string.about_title, VERSION_NAME))
            message(
                res = R.string.about_body,
                html = true,
                lineHeightMultiplier = 1.4f
            )
            positiveButton(res = R.string.common_ok)
            dialog = this
        }
    }

    override fun showUpdateDialog(updated: Boolean, error: Boolean) {
        MaterialDialog(requireContext()).show {
            title(text = if (error) "Ошибка" else "Обновление расписания")
            message(
                text = if (error)
                    "Произошла ошибка при обновлении расписания"
                else {
                    if (updated) "Расписание успешно обновлено"
                    else "Расписание в обовлении не нуждается"
                }
            )
            positiveButton(res = R.string.common_ok)
            dialog = this
        }
    }
}
