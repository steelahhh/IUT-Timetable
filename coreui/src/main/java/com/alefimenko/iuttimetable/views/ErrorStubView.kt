package com.alefimenko.iuttimetable.views

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.alefimenko.iuttimetable.coreui.R
import com.google.android.material.button.MaterialButton

/*
 * Created by Alexander Efimenko on 2019-03-10.
 */

class ErrorStubView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val title: TextView
    private val button: MaterialButton

    var retryVisible: Boolean = true
        set(value) {
            field = value
            button.isVisible = field
        }

    @StringRes
    var textRes: Int = -1
        set(value) {
            field = value
            if (textRes != -1) {
                text = context.getString(value)
            }
        }

    var text: String = ""
        set(value) {
            field = value
            title.text = value
        }

    var onRetryClick: (() -> Unit)? = null
        set(value) {
            field = value
            button.setOnClickListener {
                field?.invoke()
            }
        }

    init {
        inflate(context, R.layout.stub_error, this)

        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.background_color, typedValue, true)
        background = ColorDrawable(ContextCompat.getColor(context, typedValue.resourceId))
        title = findViewById(R.id.try_again_text)
        button = findViewById(R.id.try_again_button)
    }
}
