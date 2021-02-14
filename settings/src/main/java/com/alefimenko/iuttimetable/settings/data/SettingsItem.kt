package com.alefimenko.iuttimetable.settings.data

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.alefimenko.iuttimetable.settings.R
import com.alefimenko.iuttimetable.settings.databinding.ItemSettingsBinding
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

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
    val enabled: Boolean = true,
    val switcherVisible: Boolean
) : BindableItem<ItemSettingsBinding>() {
    override fun initializeViewBinding(view: View): ItemSettingsBinding = ItemSettingsBinding.bind(view)

    override fun isClickable(): Boolean = enabled

    override fun getLayout() = R.layout.item_settings

    override fun bind(viewHolder: ItemSettingsBinding, position: Int) = with(viewHolder) {
        settingsSwitch.isEnabled = enabled
        settingsTextSubtitle.isGone = subtitleRes == null
        subtitleRes?.let {
            settingsTextSubtitle.text = root.context.getString(it)
        }
        settingsTextTitle.text = root.context.getString(titleRes)
        iconRes?.run(settingsImage::setImageResource)
        settingsSwitch.isChecked = isChecked
        settingsSwitch.isVisible = switcherVisible
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemSettingsBinding>) {
        viewHolder.binding.settingsImage.setImageDrawable(null)
        super.unbind(viewHolder)
    }
}
