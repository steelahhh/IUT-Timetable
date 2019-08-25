package com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute

import com.alefimenko.iuttimetable.presentation.di.Screens
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupRepository
import com.alefimenko.iuttimetable.presentation.pickgroup.model.InstituteUi
import ru.terrakok.cicerone.Router
import java.util.concurrent.Executor

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

class PickInstituteInteractor(
    private val router: Router,
    private val pickGroupRepository: PickGroupRepository,
    private val executor: Executor
) {

    val institutes get() = pickGroupRepository.getInstitutes()

    fun goToGroups(form: Int, institute: InstituteUi) = executor.execute {
        router.navigateTo(Screens.PickGroupScreen(form, institute))
    }

    fun exit() = router.exit()
}
