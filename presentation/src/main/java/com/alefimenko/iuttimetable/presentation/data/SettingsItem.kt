package com.alefimenko.iuttimetable.presentation.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.alefimenko.iuttimetable.presentation.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_settings.*

/*
 * Created by Alexander Efimenko on 2019-07-12.
 */

data class SettingsItem(
    val key: SettingsItemKey,
    @StringRes
    val titleRes: Int,
    @StringRes
    val subtitleRes: Int? = null,
    @DrawableRes
    val iconRes: Int? = null,
    val isChecked: Boolean = false,
    val switcherVisible: Boolean
) : Item() {
    override fun getLayout() = R.layout.item_settings

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder) {
        settingsTextSubtitle.isGone = subtitleRes == null
        subtitleRes?.let {
            settingsTextSubtitle.text = itemView.context.getString(it)
        }
        settingsTextTitle.text = itemView.context.getString(titleRes)
        iconRes?.run(settingsImage::setImageResource)
        settingsSwitch.isChecked = isChecked
        settingsSwitch.isVisible = switcherVisible
    }

    override fun unbind(holder: GroupieViewHolder) = with(holder) {
        settingsImage.setImageDrawable(null)
        super.unbind(holder)
    }
}
