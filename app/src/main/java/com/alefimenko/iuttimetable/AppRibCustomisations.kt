package com.alefimenko.iuttimetable

import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.customisation.RibCustomisationDirectoryImpl

object AppRibCustomisations : RibCustomisationDirectory by customisations({
})

fun customisations(
    block: RibCustomisationDirectoryImpl.() -> Unit
): RibCustomisationDirectoryImpl = RibCustomisationDirectoryImpl().apply(block)
