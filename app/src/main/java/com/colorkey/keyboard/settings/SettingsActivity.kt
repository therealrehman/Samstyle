package com.colorkey.keyboard.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.colorkey.keyboard.ColorKeyApp
import com.colorkey.keyboard.R
import com.colorkey.keyboard.databinding.ActivitySettingsBinding
import com.google.android.material.tabs.TabLayoutMediator

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewPager: ViewPager2
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPager()
        setupTabs()
        setupHeader()
        checkKeyboardEnabled()
    }
    
    private fun setupViewPager() {
        viewPager = binding.viewPager
        val adapter = SettingsPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
    }
    
    private fun setupTabs() {
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_themes)
                1 -> getString(R.string.tab_effects)
                2 -> getString(R.string.tab_sounds)
                3 -> getString(R.string.tab_emoji)
                else -> ""
            }
        }.attach()
    }
    
    private fun setupHeader() {
        binding.previewKeyboard.setOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showInputMethodPicker()
        }
        binding.enableKeyboardBtn.setOnClickListener {
            startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        }
        binding.selectKeyboardBtn.setOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showInputMethodPicker()
        }
    }
    
    private fun checkKeyboardEnabled() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledMethods = inputMethodManager.enabledInputMethodList
        val isEnabled = enabledMethods.any { it.packageName == packageName }
        binding.enableKeyboardBtn.visibility = if (isEnabled) android.view.View.GONE else android.view.View.VISIBLE
        binding.selectKeyboardBtn.visibility = if (isEnabled) android.view.View.VISIBLE else android.view.View.GONE
    }
    
    override fun onResume() {
        super.onResume()
        checkKeyboardEnabled()
    }
}
