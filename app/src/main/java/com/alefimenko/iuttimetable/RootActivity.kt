package com.alefimenko.iuttimetable

import android.os.Bundle
import android.view.ViewGroup
import com.alefimenko.iuttimetable.di.component
import com.alefimenko.iuttimetable.extension.updateNavigationColor
import com.alefimenko.iuttimetable.root.builder.RootBuilder
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildContext.Companion.root

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class RootActivity : RibActivity() {
    override val rootViewGroup: ViewGroup get() = findViewById(R.id.container)

    override fun createRib(
        savedInstanceState: Bundle?
    ): Node<*> = RootBuilder(component).build(root(savedInstanceState)).node

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) updateNavigationColor()
    }
}
