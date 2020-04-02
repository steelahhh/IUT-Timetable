package com.alefimenko.iuttimetable.pick_institute.mapper

import com.alefimenko.iuttimetable.pick_institute.PickInstitute.Input
import com.alefimenko.iuttimetable.pick_institute.feature.PickInstituteFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? = null
}
