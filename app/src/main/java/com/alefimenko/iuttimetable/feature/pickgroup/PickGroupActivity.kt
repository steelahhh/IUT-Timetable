package com.alefimenko.iuttimetable.feature.pickgroup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.alefimenko.iuttimetable.BR
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.core.arch.ViewModelFactory
import com.alefimenko.iuttimetable.core.base.BaseActivity
import com.alefimenko.iuttimetable.databinding.ActivityPickGroupBinding

/*
 * Created by Alexander Efimenko on 22/11/18.
 */

class PickGroupActivity : BaseActivity<ActivityPickGroupBinding, PickGroupViewModel>() {
    override val layoutId: Int = R.layout.activity_pick_group
    override val vmId: Int = BR.viewModel
    override val viewModelClass: Class<PickGroupViewModel> = PickGroupViewModel::class.java
    override fun viewModelFactory(): ViewModelProvider.Factory = ViewModelFactory {
        PickGroupViewModel()
    }

//    private val fastAdapter = FastItemAdapter<GroupItem>().apply {
//        withOnClickListener { _, _, item, _ ->
//            Timber.i("Selected item ${item.id}")
//            true
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding.itemAdapter = fastAdapter
//        binding.groupRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        fastAdapter.set(data)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, PickGroupActivity::class.java)
    }
}