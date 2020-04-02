package com.alefimenko.iuttimetable.root

import com.alefimenko.iuttimetable.common.CanProvideContext
import com.badoo.ribs.core.Rib

interface Root : Rib {
    interface Dependency : CanProvideContext
}
