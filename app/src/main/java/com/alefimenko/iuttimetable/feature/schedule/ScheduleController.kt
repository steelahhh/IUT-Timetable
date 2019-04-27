package com.alefimenko.iuttimetable.feature.schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.alefimenko.iuttimetable.R
import com.alefimenko.iuttimetable.base.BaseController
import com.alefimenko.iuttimetable.core.data.DateInteractor
import com.alefimenko.iuttimetable.extension.changeMenuColors
import com.alefimenko.iuttimetable.extension.requireContext
import com.alefimenko.iuttimetable.feature.schedule.model.GroupInfo
import com.alefimenko.iuttimetable.feature.schedule.schedulepage.SchedulePagerAdapter
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

/*
 * Created by Alexander Efimenko on 2019-03-08.
 */

class ScheduleController(
    private val bundle: Bundle = Bundle()
) : BaseController<String, String>() {
    override var layoutRes: Int = R.layout.fragment_schedule

    private val toolbar by bind<BottomAppBar>(R.id.toolbar)
    private val viewPager by bind<ViewPager>(R.id.viewPager)
    private val scheduleTabs by bind<TabLayout>(R.id.scheduleTabs)

    // will be extracted into the ScheduleFeature later
    private val dateInteractorImpl: DateInteractor by inject()

    private val pagerAdapter by bind.stuff {
        SchedulePagerAdapter(this)
    }

    override fun onViewBound(view: View) {
        setupViews()
        selectCurrentDay(dateInteractorImpl.currentDay)
    }

    @SuppressLint("CheckResult")
    override fun onAttach(view: View) {
        super.onAttach(view)
        val groupInfo = bundle[GROUP_INFO] as? GroupInfo
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putInt(SELECTED_DAY, viewPager.currentItem)
        }
    }

    override fun acceptViewModel(viewModel: String) {
    }

    private fun setupViews() {
        viewPager.adapter = pagerAdapter
        scheduleTabs.setupWithViewPager(viewPager)

        toolbar.apply {
            replaceMenu(R.menu.schedule_menu)
            setNavigationOnClickListener {
            }
            setOnMenuItemClickListener {
                val text = when (it.itemId) {
                    R.id.action_settings -> "Настройки"
                    R.id.action_change_week -> "Смена недели"
                    R.id.action_refresh -> "Обновить"
                    else -> ""
                }
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
                true
            }
            changeMenuColors()
        }
    }

    private fun selectCurrentDay(day: Int) {
        scheduleTabs.getTabAt(day)?.select()
    }

    companion object {
        const val TAG = "ScheduleController"
        const val SELECTED_DAY = "selected_day"
        private const val GROUP_INFO = "GROUP_INFO_KEY"

        fun newInstance(groupInfo: GroupInfo) = ScheduleController(
            Bundle().apply {
                putParcelable(GROUP_INFO, groupInfo)
            }
        )
    }
}
