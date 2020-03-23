package com.alefimenko.iuttimetable.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alefimenko.iuttimetable.coreui.R
import com.alefimenko.iuttimetable.createBinder
import com.alefimenko.iuttimetable.extension.updateNavigationColor

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
        if (savedInstanceState == null) updateNavigationColor()
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
}
