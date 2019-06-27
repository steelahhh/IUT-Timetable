package com.alefimenko.iuttimetable.presentation.schedule.model

import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.data.remote.model.ClassEntry
import com.alefimenko.iuttimetable.extension.getColorCompat
import com.alefimenko.iuttimetable.extension.hide
import com.alefimenko.iuttimetable.extension.show
import com.alefimenko.iuttimetable.presentation.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_class.view.*

/*
 * Created by Alexander Efimenko on 2019-05-09.
 */

enum class Position {
    FIRST,
    OTHER,
    LAST,
    SINGLE
}

data class ClassUi(
    val subject: String = "",
    val teacher: String = "",
    val classType: String = "",
    val startTime: String = "",
    val finishTime: String = "",
    val location: String = "",
    val date: String = Constants.EMPTY_ENTRY,
    val innerGroup: String = Constants.EMPTY_ENTRY,
    val hidden: Boolean = false,
    val positionInGroup: Position
) : Item() {
    override fun getLayout() = R.layout.item_class

    override fun bind(viewHolder: ViewHolder, position: Int) = with(viewHolder.itemView) {
        renderBackground()
        finishTimeTv.text = finishTime
        startTimeTv.text = startTime
        subjectTv.text = subject
        teacherTv.text = teacher
        typeTv.text = classType
        locationTv.text = location
        innerGroupTv.isVisible = innerGroup != Constants.EMPTY_ENTRY
        innerGroupTv.text = innerGroup
        menuBtn.setOnClickListener {
            // set click listener for menu
        }
        if (this@ClassUi.hidden) {
//            setBackgroundColor(Color.LTGRAY)
            finishTimeTv.hide()
            locationTv.hide()
            teacherTv.hide()
            innerGroupTv.hide()
            typeTv.hide()
            hidden.show()
            startTimeTv.setTextColor(context.getColorCompat(R.color.warmGray))
            subjectTv.setTextColor(context.getColorCompat(R.color.warmGray))
            hidden.setColorFilter(context.getColorCompat(R.color.warmGray))
            menuBtn.setColorFilter(context.getColorCompat(R.color.warmGray))

            subjectTv.textSize = 15f
            startTimeTv.textSize = 15f

            ConstraintSet().apply {
                clone(class_layout)
                connect(R.id.startTimeTv, ConstraintSet.BOTTOM, R.id.class_layout, ConstraintSet.BOTTOM, 0)
                connect(R.id.startTimeTv, ConstraintSet.TOP, R.id.class_layout, ConstraintSet.TOP, 0)
                connect(R.id.startTimeTv, ConstraintSet.LEFT, R.id.class_layout, ConstraintSet.LEFT, 0)
                connect(R.id.subjectTv, ConstraintSet.TOP, R.id.class_layout, ConstraintSet.TOP, 0)
                connect(R.id.subjectTv, ConstraintSet.BOTTOM, R.id.class_layout, ConstraintSet.BOTTOM, 0)
                connect(R.id.subjectTv, ConstraintSet.END, R.id.hidden, ConstraintSet.START, 0)
                connect(R.id.subjectTv, ConstraintSet.START, R.id.startTimeTv, ConstraintSet.END, 24)
                connect(R.id.menuBtn, ConstraintSet.TOP, R.id.class_layout, ConstraintSet.TOP, 0)
                connect(R.id.menuBtn, ConstraintSet.BOTTOM, R.id.class_layout, ConstraintSet.BOTTOM, 0)
                connect(R.id.menuBtn, ConstraintSet.RIGHT, R.id.class_layout, ConstraintSet.RIGHT, 0)
                connect(R.id.hidden, ConstraintSet.TOP, R.id.class_layout, ConstraintSet.TOP, 0)
                connect(R.id.hidden, ConstraintSet.RIGHT, R.id.menuBtn, ConstraintSet.LEFT, 0)
                connect(R.id.hidden, ConstraintSet.BOTTOM, R.id.class_layout, ConstraintSet.BOTTOM, 0)
                constrainDefaultWidth(R.id.subjectTv, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
                applyTo(class_layout)
            }
        }
    }

    private fun View.renderBackground() {
        val drawableRes = when (positionInGroup) {
            Position.FIRST -> R.drawable.card_background_first
            Position.OTHER -> R.drawable.card_background_other
            Position.LAST -> R.drawable.card_background_last
            Position.SINGLE -> R.drawable.card_background_single
        }
        divider.isGone = positionInGroup == Position.LAST || positionInGroup == Position.SINGLE
        val drawable = ContextCompat.getDrawable(context, drawableRes)
        val color = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> ContextCompat.getColor(context, R.color.almostDark)
            else -> ContextCompat.getColor(
                context,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) R.color.white else R.color.slightlyWhiteGray
            )
        }
        drawable?.mutate()
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        background = drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 12f
        }
    }
}

fun ClassEntry.toClassUi(position: Position) = ClassUi(
    subject = subject,
    teacher = teacher,
    classType = classType,
    startTime = time.start,
    finishTime = time.finish,
    location = location,
    date = date,
    innerGroup = innerGroup,
    hidden = hidden,
    positionInGroup = position
)
