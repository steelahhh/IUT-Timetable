package com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alefimenko.iuttimetable.base.BaseController
import com.alefimenko.iuttimetable.presentation.di.Scopes
import com.spotify.mobius.MobiusLoop
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope

/*
 * Created by Alexander Efimenko on 2019-02-04.
 */

class PickInstituteController : BaseController<Nothing, Nothing>() {

    private val scope = getOrCreateScope(Scopes.PICK_GROUP)

    private val controller: MobiusLoop.Controller<Model, Event> by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = PickInstituteView(inflater, container)
        bindScope(scope)
        controller.connect(view)
        controller.start()
        return view.containerView
    }

    override fun onDestroyView(view: View) {
        controller.stop()
        controller.disconnect()
        super.onDestroyView(view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("MODEL", controller.model)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        controller.replaceModel(savedInstanceState["MODEL"] as Model)
    }

    override fun acceptViewModel(viewModel: Nothing) = Unit
}
