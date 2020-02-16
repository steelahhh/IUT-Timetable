package com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alefimenko.iuttimetable.base.BaseController
import com.alefimenko.iuttimetable.presentation.di.Scopes
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteFeature.Event
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteFeature.InstituteEffectHandler
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteFeature.InstituteInitializer
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteFeature.InstituteUpdater
import com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute.PickInstituteFeature.Model
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid.controller
import com.spotify.mobius.rx2.RxMobius
import org.koin.android.ext.android.getKoin
import org.koin.androidx.scope.bindScope
import org.koin.core.qualifier.named

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

class PickInstituteController(
    bundle: Bundle = Bundle()
) : BaseController() {
    private var isFromSchedule: Boolean = false
    private val scope = getKoin().getOrCreateScope(Scopes.PICK_GROUP, named(Scopes.PICK_GROUP))
    private var pickInstituteView: PickInstituteView? = null

    init {
        isFromSchedule = bundle.getBoolean(IS_FROM_SCHEDULE)
    }

    private val controller: MobiusLoop.Controller<Model, Event> = controller(
        RxMobius.loop(
            InstituteUpdater,
            InstituteEffectHandler(scope.get(), scope.get()).create()
        ).init(InstituteInitializer).logger(AndroidLogger.tag("PIFFER")),
        Model()
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = PickInstituteView(inflater, container).apply {
            pickInstituteView = this
            bindScope(scope)
            controller.connect(this)
            controller.start()
            this.isFromSchedule = this@PickInstituteController.isFromSchedule
        }
        return view.containerView
    }

    override fun onDestroyView(view: View) {
        controller.stop()
        controller.disconnect()
        super.onDestroyView(view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(IS_FROM_SCHEDULE, isFromSchedule)
        outState.putParcelable(MODEL, controller.model)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        isFromSchedule = savedInstanceState.getBoolean(IS_FROM_SCHEDULE)
        controller.replaceModel(savedInstanceState[MODEL] as Model)
        pickInstituteView?.isFromSchedule = isFromSchedule
    }

    companion object {
        const val MODEL = "MODEl"
        const val IS_FROM_SCHEDULE = "is_from_schedule"

        fun newInstance(
            isFromSchedule: Boolean
        ): PickInstituteController = PickInstituteController(
            Bundle().apply {
                putBoolean(IS_FROM_SCHEDULE, isFromSchedule)
            }
        )
    }
}
