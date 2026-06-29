package com.colorkey.keyboard.settings

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SettingsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ThemesFragment()
            1 -> EffectsFragment()
            2 -> SoundsFragment()
            3 -> EmojiFragment()
            else -> ThemesFragment()
        }
    }
}
