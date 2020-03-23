package com.alefimenko.iuttimetable.root

import com.alefimenko.iuttimetable.common.ContextProvider
import com.badoo.ribs.core.Rib

interface Root : Rib {
    interface Dependency : ContextProvider
}
