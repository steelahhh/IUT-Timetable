package com.alefimenko.iuttimetable.presentation.pickgroup.pickinstitute

import com.alefimenko.iuttimetable.presentation.di.Screens
import com.alefimenko.iuttimetable.presentation.pickgroup.PickGroupRepository
import ru.terrakok.cicerone.Router

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

class PickInstituteInteractor(
    private val router: Router,
    private val pickGroupRepository: PickGroupRepository
) {

    val institutes get() = pickGroupRepository.getInstitutes()

    fun navigate() = router.navigateTo(Screens.PickInstituteScreen())

    fun exit() = router.exit()
}
