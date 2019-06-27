package com.alefimenko.iuttimetable.presentation.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alefimenko.iuttimetable.base.BaseController
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleFeature.Event
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleFeature.Model
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleFeature.ScheduleEffectHandler
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleFeature.ScheduleInitializer
import com.alefimenko.iuttimetable.presentation.schedule.ScheduleFeature.ScheduleUpdater
import com.alefimenko.iuttimetable.presentation.schedule.model.GroupInfo
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid.controller
import com.spotify.mobius.rx2.RxMobius
import org.koin.android.ext.android.get

/*
 * Created by Alexander Efimenko on 2019-03-08.
 */

interface ScheduleViewContract {
    fun switchToCurrentDay(day: Int)
}

class ScheduleController(
    bundle: Bundle = Bundle()
) : BaseController(), ScheduleViewContract {

    private val controller: MobiusLoop.Controller<Model, Event> = controller(
        RxMobius.loop(
            ScheduleUpdater,
            ScheduleEffectHandler(get(), get(), get(), this).create()
        ).init(ScheduleInitializer).logger(AndroidLogger.tag("SCHEDULE")),
        Model(groupInfo = bundle[GROUP_INFO] as? GroupInfo)
    )

    private var scheduleView: ScheduleView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return ScheduleView(inflater, container).apply {
            controller.connect(connector)
            controller.start()
            scheduleView = this
        }.containerView
    }

    override fun switchToCurrentDay(day: Int) = post {
        scheduleView?.switchToCurrent(day)
    }

    override fun onDestroyView(view: View) {
        controller.stop()
        controller.disconnect()
        super.onDestroyView(view)
    }

    companion object {
        const val TAG = "ScheduleController"
        private const val GROUP_INFO = "GROUP_INFO_KEY"

        fun newInstance(groupInfo: GroupInfo) =
            ScheduleController(
                Bundle().apply {
                    putParcelable(
                        GROUP_INFO,
                        groupInfo
                    )
                }
            )
    }
}
