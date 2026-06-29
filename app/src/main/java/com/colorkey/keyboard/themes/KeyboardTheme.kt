package com.colorkey.keyboard.themes

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

data class KeyboardTheme(
    val id: String,
    val nameRes: Int,
    val previewGradient: IntArray,
    val keyBackgroundColor: Int,
    val keyTextColor: Int,
    val keyboardBackgroundColor: Int,
    val accentColor: Int,
    val isDark: Boolean,
    val keyCornerRadius: Float,
    val keyElevation: Float,
    val glowIntensity: Float = 0f
) {
    fun createPreviewDrawable(): GradientDrawable {
        return GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            previewGradient
        ).apply {
            cornerRadius = 24f
        }
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KeyboardTheme) return false
        return id == other.id
    }
    
    override fun hashCode(): Int = id.hashCode()
}
