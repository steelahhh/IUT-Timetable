package com.alefimenko.iuttimetable.presentation.schedule.schedulepage

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.alefimenko.iuttimetable.data.remote.model.ClassEntry
import com.alefimenko.iuttimetable.data.remote.model.hasInnerGroup
import com.alefimenko.iuttimetable.extension.getColorCompat
import com.alefimenko.iuttimetable.extension.hide
import com.alefimenko.iuttimetable.extension.show
import com.alefimenko.iuttimetable.presentation.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_class.view.*

/*
 * Created by Alexander Efimenko on 2019-05-09.
 */

data class ClassUi(
    val classEntry: ClassEntry
) : AbstractItem<ClassUi.VH>() {
    override val type get() = R.id.class_layout
    override fun getViewHolder(v: View) = VH(v)
    override val layoutRes get() = R.layout.item_class

    class VH(view: View) : FastAdapter.ViewHolder<ClassUi>(view) {
        override fun unbindView(item: ClassUi) = with(itemView) {
            menu.setOnClickListener(null)
        }

        override fun bindView(item: ClassUi, payloads: MutableList<Any>) = with(itemView) {
            val current = item.classEntry
            finishTime.text = current.time.finish
            startTime.text = current.time.start
            subject.text = current.subject
            teacher.text = current.teacher
            type.text = current.classType
            location.text = current.location
            innerGroup.isVisible = current.hasInnerGroup
            innerGroup.text = current.innerGroup
            menu.setOnClickListener {
                // set click listener for menu
            }
            if (current.hidden) {
//                background = ActivityUtils.getDrawable(context, R.color.ultralight_grey)
                finishTime.hide()
                location.hide()
                teacher.hide()
                innerGroup.hide()
                type.hide()
                hidden.show()
                startTime.setTextColor(context.getColorCompat(R.color.warmGray))
                subject.setTextColor(context.getColorCompat(R.color.warmGray))
                hidden.setColorFilter(context.getColorCompat(R.color.warmGray))
                menu.setColorFilter(context.getColorCompat(R.color.warmGray))

                subject.textSize = 15f
                startTime.textSize = 15f

                val constraintSet = ConstraintSet()
                constraintSet.apply {
                    clone(class_layout)
                    connect(R.id.startTime, ConstraintSet.BOTTOM, R.id.class_layout, ConstraintSet.BOTTOM, 0)
                    connect(R.id.startTime, ConstraintSet.TOP, R.id.class_layout, ConstraintSet.TOP, 0)
                    connect(R.id.startTime, ConstraintSet.LEFT, R.id.class_layout, ConstraintSet.LEFT, 0)
                    connect(R.id.subject, ConstraintSet.TOP, R.id.class_layout, ConstraintSet.TOP, 0)
                    connect(R.id.subject, ConstraintSet.BOTTOM, R.id.class_layout, ConstraintSet.BOTTOM, 0)
                    connect(R.id.subject, ConstraintSet.END, R.id.hidden, ConstraintSet.START, 0)
                    connect(R.id.subject, ConstraintSet.START, R.id.startTime, ConstraintSet.END, 24)
                    connect(R.id.menu, ConstraintSet.TOP, R.id.class_layout, ConstraintSet.TOP, 0)
                    connect(R.id.menu, ConstraintSet.BOTTOM, R.id.class_layout, ConstraintSet.BOTTOM, 0)
                    connect(R.id.menu, ConstraintSet.RIGHT, R.id.class_layout, ConstraintSet.RIGHT, 0)
                    connect(R.id.hidden, ConstraintSet.TOP, R.id.class_layout, ConstraintSet.TOP, 0)
                    connect(R.id.hidden, ConstraintSet.RIGHT, R.id.menu, ConstraintSet.LEFT, 0)
                    connect(R.id.hidden, ConstraintSet.BOTTOM, R.id.class_layout, ConstraintSet.BOTTOM, 0)
                    constrainDefaultWidth(R.id.subject, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
                    applyTo(class_layout)
                }
            }
        }
    }
}
