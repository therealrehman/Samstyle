package com.colorkey.keyboard.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.colorkey.keyboard.ColorKeyApp
import com.colorkey.keyboard.R
import com.colorkey.keyboard.databinding.FragmentThemesBinding
import com.colorkey.keyboard.themes.KeyboardTheme

class ThemesFragment : Fragment() {
    
    private var _binding: FragmentThemesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ThemeAdapter
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentThemesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupMasterThemeToggle()
    }
    
    private fun setupRecyclerView() {
        val themes = ColorKeyApp.instance.themeManager.allThemes
        adapter = ThemeAdapter(themes) { theme -> onThemeSelected(theme) }
        binding.themesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@ThemesFragment.adapter
            setHasFixedSize(true)
        }
        val currentTheme = ColorKeyApp.instance.themeManager.currentTheme
        adapter.setSelectedTheme(currentTheme.id)
    }
    
    private fun setupMasterThemeToggle() {
        val prefs = requireContext().getSharedPreferences("colorkey_settings", 0)
        binding.masterThemeSwitch.isChecked = prefs.getBoolean("master_theme_enabled", true)
        binding.masterThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("master_theme_enabled", isChecked).apply()
            if (isChecked) {
                Toast.makeText(context, "Master Theme: Radial color animation enabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun onThemeSelected(theme: KeyboardTheme) {
        ColorKeyApp.instance.themeManager.currentTheme = theme
        adapter.setSelectedTheme(theme.id)
        Toast.makeText(context, getString(R.string.toast_theme_applied), Toast.LENGTH_SHORT).show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
