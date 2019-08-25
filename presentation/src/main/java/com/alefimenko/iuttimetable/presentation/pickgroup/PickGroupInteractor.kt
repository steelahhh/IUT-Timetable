package com.alefimenko.iuttimetable.presentation.pickgroup

import ru.terrakok.cicerone.Router

/*
 * Created by Alexander Efimenko on 2019-08-24.
 */

class PickGroupInteractor(
    private val router: Router,
    private val pickGroupRepository: PickGroupRepository
) {

    fun getGroups(form: Int, instituteId: Int) =
        pickGroupRepository.getGroups(form, instituteId)

    fun exit() = router.exit()
}
