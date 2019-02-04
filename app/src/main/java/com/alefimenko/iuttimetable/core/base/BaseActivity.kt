package com.alefimenko.iuttimetable.core.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.util.changeToolbarFont
import com.alefimenko.iuttimetable.util.createBinder

/*
 * Created by Alexander Efimenko on 21/11/18.
 */

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    protected abstract val layoutId: Int

    protected val bind = createBinder()

    protected var lastClickTime: Long = 0

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

    protected inline fun debouncedAction(action: () -> Unit) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
        action()
    }

    fun setupToolbar(homeAsUp: Boolean = false) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        if (toolbar != null) {
            setSupportActionBar(toolbar)

            toolbar.changeToolbarFont()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUp)
    }
}
