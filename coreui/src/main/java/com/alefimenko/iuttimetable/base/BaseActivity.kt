package com.alefimenko.iuttimetable.base

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.github.steelahhh.coreui.R
import com.alefimenko.iuttimetable.createBinder

/*
 * Created by Alexander Efimenko on 21/11/18.
 */

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    protected abstract val layoutId: Int

    protected val bind = createBinder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }

    override fun onDestroy() {
        super.onDestroy()
        bind.resetViews()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun updateNavigationColor() {
        val typedValueAttr = TypedValue()
        theme.resolveAttribute(R.attr.background_color, typedValueAttr, true)
        val color = ContextCompat.getColor(this, typedValueAttr.resourceId)
        val darkColor = ContextCompat.getColor(this, R.color.backgroundDark)
        val isDark = color == darkColor

        var newNavigationColor = color

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                statusBarColor = color
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !isDark) {
                    var flags = decorView.systemUiVisibility
                    flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    decorView.systemUiVisibility = flags
                } else {
                    newNavigationColor = ContextCompat.getColor(
                        this@BaseActivity,
                        if (isDark) {
                            R.color.backgroundDark
                        } else {
                            android.R.color.black
                        }
                    )
                }
                navigationBarColor = newNavigationColor
            }
        }
    }
}
