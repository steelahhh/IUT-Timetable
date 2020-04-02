package com.alefimenko.iuttimetable.schedule.data.model

import android.graphics.PorterDuff
import android.os.Build
import android.text.TextUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.BASELINE
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.alefimenko.iuttimetable.data.local.Constants
import com.alefimenko.iuttimetable.data.remote.model.ClassEntry
import com.alefimenko.iuttimetable.extension.getColorCompat
import com.alefimenko.iuttimetable.extension.getDimenPixelSize
import com.alefimenko.iuttimetable.extension.getPrimaryTextColor
import com.alefimenko.iuttimetable.extension.isDarkModeEnabled
import com.alefimenko.iuttimetable.schedule.R
import com.alefimenko.iuttimetable.schedule.databinding.ItemClassBinding
import com.xwray.groupie.viewbinding.BindableItem

/*
 * Created by Alexander Efimenko on 2019-05-09.
 */

typealias OnClassMenuClick = (classItem: ClassItem, view: View) -> Unit

data class ClassItem(
    val subject: String = "",
    val teacher: String = "",
    val classType: String = "",
    val startTime: String = "",
    val finishTime: String = "",
    val location: String = "",
    val date: String = Constants.EMPTY_ENTRY,
    val innerGroup: String = Constants.EMPTY_ENTRY,
    val hidden: Boolean,
    val positionInGroup: Position,
    val onClassMenuClick: OnClassMenuClick
) : BindableItem<ItemClassBinding>() {
    override fun getLayout() = R.layout.item_class

    override fun initializeViewBinding(view: View): ItemClassBinding = ItemClassBinding.bind(view)

    override fun bind(viewHolder: ItemClassBinding, position: Int) = with(viewHolder) {
        renderBackground()
        updateConstraints()
        finishTimeTv.text = finishTime
        startTimeTv.text = startTime
        subjectTv.text = subject
        teacherTv.text = teacher
        typeTv.text = classType
        locationTv.text = location
        innerGroupTv.isGone = hidden || innerGroup == Constants.EMPTY_ENTRY
        innerGroupTv.text = innerGroup
        menuBtn.setOnClickListener {
            onClassMenuClick(this@ClassItem, it)
        }

        finishTimeTv.isGone = hidden
        locationTv.isGone = hidden
        teacherTv.isGone = hidden
        typeTv.isGone = hidden
        hiddenIv.isGone = !hidden

        val textColor = if (hidden) root.context.getColorCompat(R.color.warmGray) else {
            root.context.getPrimaryTextColor()
        }

        startTimeTv.setTextColor(textColor)
        subjectTv.setTextColor(textColor)
        hiddenIv.setColorFilter(textColor)
        menuBtn.setColorFilter(textColor)
    }

    private fun ItemClassBinding.renderBackground() {
        val context = root.context
        val drawableRes = when (positionInGroup) {
            Position.FIRST -> R.drawable.card_background_first
            Position.OTHER -> R.drawable.card_background_other
            Position.LAST -> R.drawable.card_background_last
            Position.SINGLE -> R.drawable.card_background_single
        }
        divider.isGone = positionInGroup == Position.LAST || positionInGroup == Position.SINGLE
        val drawable = ContextCompat.getDrawable(context, drawableRes)
        val color = when (context.isDarkModeEnabled) {
            true -> context.getColorCompat(R.color.almostDark)
            else -> context.getColorCompat(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) R.color.white else R.color.slightlyWhiteGray
            )
        }
        drawable?.mutate()
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        root.background = drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            root.elevation = 12f
        }
    }

    private fun ItemClassBinding.updateConstraints() {
        val context = root.context
        val constraints = ConstraintSet().apply {
            clone(root)
        }
        if (hidden) {
            constraints.apply {
                subjectTv.isSingleLine = true
                subjectTv.marqueeRepeatLimit = 3
                subjectTv.ellipsize = TextUtils.TruncateAt.END
                connect(startTimeTv.id, TOP, PARENT_ID, TOP, 17)
                connect(subjectTv.id, TOP, PARENT_ID, TOP, 17)
                connect(subjectTv.id, BOTTOM, PARENT_ID, BOTTOM, 17)
                connect(
                    subjectTv.id, END, hiddenIv.id, START,
                    context.getDimenPixelSize(R.dimen.spacing_4)
                )
                connect(subjectTv.id, BASELINE, startTimeTv.id, BASELINE, 0)
                connect(hiddenIv.id, TOP, PARENT_ID, TOP, 10)
                connect(hiddenIv.id, BOTTOM, PARENT_ID, BOTTOM, 10)
                applyTo(root)
            }
        } else {
            subjectTv.isSingleLine = false
            subjectTv.marqueeRepeatLimit = -1
            constraints.apply {
                connect(
                    subjectTv.id, TOP, typeTv.id, BOTTOM,
                    context.getDimenPixelSize(R.dimen.spacing_4)
                )
                applyTo(root)
            }
        }
    }
}

fun ClassEntry.toClassItem(position: Position, onClassMenuClick: OnClassMenuClick) = ClassItem(
    subject = subject,
    teacher = teacher,
    classType = classType,
    startTime = time.start,
    finishTime = time.finish,
    location = location,
    date = date,
    innerGroup = innerGroup,
    hidden = hidden,
    positionInGroup = position,
    onClassMenuClick = onClassMenuClick
)
