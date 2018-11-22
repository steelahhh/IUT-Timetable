package com.alefimenko.iuttimetable.core.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.util.changeToolbarFont

/*
 * Created by Alexander Efimenko on 21/11/18.
 */

@SuppressLint("Registered")
abstract class BaseActivity<DB : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {
    protected lateinit var binding: DB
    protected abstract val layoutId: Int

    protected abstract val vmId: Int
    protected lateinit var vm: VM
    protected abstract val viewModelClass: Class<VM>
    protected abstract fun viewModelFactory(): ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this, viewModelFactory())[viewModelClass]

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.setLifecycleOwner(this)
        binding.setVariable(vmId, vm)
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

    fun setupToolbar(homeAsUp: Boolean = false) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        if (toolbar != null) {
            setSupportActionBar(toolbar)

            toolbar.changeToolbarFont()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUp)
    }
}
