package com.colorkey.keyboard.themes

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.core.content.edit
import com.colorkey.keyboard.R
import com.google.gson.Gson

class ThemeManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    var currentTheme: KeyboardTheme
        get() {
            val json = prefs.getString(KEY_CURRENT_THEME, null)
            return if (json != null) {
                gson.fromJson(json, KeyboardTheme::class.java)
            } else {
                DEFAULT_THEME
            }
        }
        set(value) {
            prefs.edit { putString(KEY_CURRENT_THEME, gson.toJson(value)) }
        }
    
    val allThemes: List<KeyboardTheme> = listOf(
        DEFAULT_THEME, NEON_THEME, PASTEL_THEME, OCEAN_THEME,
        SUNSET_THEME, MIDNIGHT_THEME, RAINBOW_THEME, CYBER_THEME
    )
    
    fun getThemeById(id: String): KeyboardTheme {
        return allThemes.find { it.id == id } ?: DEFAULT_THEME
    }
    
    companion object {
        private const val PREFS_NAME = "colorkey_themes"
        private const val KEY_CURRENT_THEME = "current_theme"
        
        val DEFAULT_THEME = KeyboardTheme(
            id = "classic", nameRes = R.string.theme_classic,
            previewGradient = intArrayOf(Color.parseColor("#FF6B6B"), Color.parseColor("#4ECDC4")),
            keyBackgroundColor = Color.parseColor("#F0F0F0"),
            keyTextColor = Color.parseColor("#2C3E50"),
            keyboardBackgroundColor = Color.parseColor("#FAFAFA"),
            accentColor = Color.parseColor("#FF6B6B"),
            isDark = false, keyCornerRadius = 12f, keyElevation = 4f
        )
        
        val NEON_THEME = KeyboardTheme(
            id = "neon", nameRes = R.string.theme_neon,
            previewGradient = intArrayOf(Color.parseColor("#FF006E"), Color.parseColor("#00F5FF"), Color.parseColor("#BF00FF")),
            keyBackgroundColor = Color.parseColor("#1A1A2E"),
            keyTextColor = Color.parseColor("#FFFFFF"),
            keyboardBackgroundColor = Color.parseColor("#0F0F1A"),
            accentColor = Color.parseColor("#FF006E"),
            isDark = true, keyCornerRadius = 16f, keyElevation = 8f, glowIntensity = 0.8f
        )
        
        val PASTEL_THEME = KeyboardTheme(
            id = "pastel", nameRes = R.string.theme_pastel,
            previewGradient = intArrayOf(Color.parseColor("#FFB3BA"), Color.parseColor("#BAE1FF"), Color.parseColor("#BAFFC9"), Color.parseColor("#FFFFBA")),
            keyBackgroundColor = Color.parseColor("#FFF5F5"),
            keyTextColor = Color.parseColor("#5D4E6D"),
            keyboardBackgroundColor = Color.parseColor("#FFF8F8"),
            accentColor = Color.parseColor("#FFB3BA"),
            isDark = false, keyCornerRadius = 20f, keyElevation = 2f
        )
        
        val OCEAN_THEME = KeyboardTheme(
            id = "ocean", nameRes = R.string.theme_ocean,
            previewGradient = intArrayOf(Color.parseColor("#006994"), Color.parseColor("#0096C7"), Color.parseColor("#48CAE4"), Color.parseColor("#CAF0F8")),
            keyBackgroundColor = Color.parseColor("#E8F4F8"),
            keyTextColor = Color.parseColor("#003D5C"),
            keyboardBackgroundColor = Color.parseColor("#F0F8FB"),
            accentColor = Color.parseColor("#0096C7"),
            isDark = false, keyCornerRadius = 14f, keyElevation = 3f
        )
        
        val SUNSET_THEME = KeyboardTheme(
            id = "sunset", nameRes = R.string.theme_sunset,
            previewGradient = intArrayOf(Color.parseColor("#FF4500"), Color.parseColor("#FF8C00"), Color.parseColor("#FFD700"), Color.parseColor("#FF69B4")),
            keyBackgroundColor = Color.parseColor("#FFF0E6"),
            keyTextColor = Color.parseColor("#8B2500"),
            keyboardBackgroundColor = Color.parseColor("#FFF5EE"),
            accentColor = Color.parseColor("#FF4500"),
            isDark = false, keyCornerRadius = 12f, keyElevation = 4f
        )
        
        val MIDNIGHT_THEME = KeyboardTheme(
            id = "midnight", nameRes = R.string.theme_midnight,
            previewGradient = intArrayOf(Color.parseColor("#0F0F23"), Color.parseColor("#1A1A3E"), Color.parseColor("#2D1B69"), Color.parseColor("#4A148C")),
            keyBackgroundColor = Color.parseColor("#1A1A3E"),
            keyTextColor = Color.parseColor("#E0E0FF"),
            keyboardBackgroundColor = Color.parseColor("#0F0F23"),
            accentColor = Color.parseColor("#7C4DFF"),
            isDark = true, keyCornerRadius = 8f, keyElevation = 6f, glowIntensity = 0.5f
        )
        
        val RAINBOW_THEME = KeyboardTheme(
            id = "rainbow", nameRes = R.string.theme_rainbow,
            previewGradient = intArrayOf(Color.parseColor("#FF0000"), Color.parseColor("#FF7F00"), Color.parseColor("#FFFF00"), Color.parseColor("#00FF00"), Color.parseColor("#0000FF"), Color.parseColor("#4B0082"), Color.parseColor("#9400D3")),
            keyBackgroundColor = Color.parseColor("#FFFFFF"),
            keyTextColor = Color.parseColor("#333333"),
            keyboardBackgroundColor = Color.parseColor("#F8F8F8"),
            accentColor = Color.parseColor("#FF0000"),
            isDark = false, keyCornerRadius = 24f, keyElevation = 2f
        )
        
        val CYBER_THEME = KeyboardTheme(
            id = "cyber", nameRes = R.string.theme_cyber,
            previewGradient = intArrayOf(Color.parseColor("#00FF41"), Color.parseColor("#008F11"), Color.parseColor("#003B00"), Color.parseColor("#0D0208")),
            keyBackgroundColor = Color.parseColor("#0D0208"),
            keyTextColor = Color.parseColor("#00FF41"),
            keyboardBackgroundColor = Color.parseColor("#050A05"),
            accentColor = Color.parseColor("#00FF41"),
            isDark = true, keyCornerRadius = 4f, keyElevation = 0f, glowIntensity = 1.0f
        )
    }
}
