package com.colorkey.keyboard

import android.app.Application
import com.colorkey.keyboard.sound.SoundManager
import com.colorkey.keyboard.themes.ThemeManager
import com.colorkey.keyboard.effects.EffectManager

class ColorKeyApp : Application() {
    
    companion object {
        lateinit var instance: ColorKeyApp
            private set
    }
    
    lateinit var themeManager: ThemeManager
        private set
    lateinit var effectManager: EffectManager
        private set
    lateinit var soundManager: SoundManager
        private set
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        themeManager = ThemeManager(this)
        effectManager = EffectManager(this)
        soundManager = SoundManager(this)
    }
}
