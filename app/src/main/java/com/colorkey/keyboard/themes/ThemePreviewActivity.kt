package com.colorkey.keyboard.themes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.colorkey.keyboard.ColorKeyApp
import com.colorkey.keyboard.databinding.ActivityThemePreviewBinding

class ThemePreviewActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityThemePreviewBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val themeId = intent.getStringExtra("theme_id") ?: return
        val theme = ColorKeyApp.instance.themeManager.getThemeById(themeId)
        
        binding.previewContainer.setBackgroundColor(theme.keyboardBackgroundColor)
        binding.applyButton.setOnClickListener {
            ColorKeyApp.instance.themeManager.currentTheme = theme
            finish()
        }
    }
}
