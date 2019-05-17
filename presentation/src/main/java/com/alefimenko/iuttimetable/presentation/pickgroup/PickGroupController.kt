package com.alefimenko.iuttimetable.presentation.pickgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alefimenko.iuttimetable.base.BaseController
import com.alefimenko.iuttimetable.extension.hideSoftKeyboard
import com.alefimenko.iuttimetable.presentation.R
import com.alefimenko.iuttimetable.presentation.di.Scopes
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.Event
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.GroupInitializer
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.GroupUpdater
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.Model
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupFeature.PickGroupEffectHandler
import com.alefimenko.iuttimetable.presentation.pickgroup.model.InstituteUi
import com.google.android.material.bottomappbar.BottomAppBar
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid.controller
import com.spotify.mobius.rx2.RxMobius
import org.koin.android.ext.android.get
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope

/*
 * Created by Alexander Efimenko on 2019-03-02.
 */

class PickGroupController(
    bundle: Bundle = Bundle()
) : BaseController() {
    private var form: Int
    private var institute: InstituteUi?

    init {
        form = bundle.getInt(FORM_KEY)
        institute = bundle.getParcelable(INSTITUTE_KEY)
    }

    private val scope = getOrCreateScope(Scopes.PICK_GROUP)

    private val controller: MobiusLoop.Controller<Model, Event> = controller(
        RxMobius.loop(
            GroupUpdater,
            PickGroupEffectHandler(get(), get()).create()
        ).init(GroupInitializer).logger(AndroidLogger.tag("PINSTER")),
        Model(form = form, institute = institute)
    )
    private val toolbar by bind<BottomAppBar>(R.id.toolbar)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        val view = PickGroupView(inflater, container)
        bindScope(scope)
        controller.connect(view)
        controller.start()
        return view.containerView
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        toolbar.setNavigationOnClickListener {
            router.popCurrentController()
        }
    }

    override fun onDestroyView(view: View) {
        view.hideSoftKeyboard()
        controller.stop()
        controller.disconnect()
        toolbar.setNavigationOnClickListener(null)
        super.onDestroyView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putParcelable(INSTITUTE_KEY, institute)
            putInt(FORM_KEY, form)
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        form = savedInstanceState[FORM_KEY] as Int
        institute = savedInstanceState[INSTITUTE_KEY] as InstituteUi
    }

    companion object {
        const val TAG = "PickGroupScreen"
        const val FORM_KEY = "FORM"
        const val INSTITUTE_KEY = "INSTITUTE"

        fun newInstance(
            form: Int,
            institute: InstituteUi
        ): PickGroupController = PickGroupController(
            Bundle().apply {
                putInt(FORM_KEY, form)
                putParcelable(INSTITUTE_KEY, institute)
            }
        )

        fun createBundle(
            form: Int,
            institute: InstituteUi
        ) = Bundle().apply {
            putInt(FORM_KEY, form)
            putParcelable(INSTITUTE_KEY, institute)
        }
    }
}
