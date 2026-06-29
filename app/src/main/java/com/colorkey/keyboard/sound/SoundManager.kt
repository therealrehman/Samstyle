package com.colorkey.keyboard.sound

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.edit
import com.colorkey.keyboard.R

class SoundManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private var soundPool: SoundPool
    private val soundMap = mutableMapOf<SoundType, Int>()
    
    var currentSound: SoundType
        get() {
            val name = prefs.getString(KEY_CURRENT_SOUND, SoundType.MECHANICAL.name) ?: SoundType.MECHANICAL.name
            return SoundType.valueOf(name)
        }
        set(value) {
            prefs.edit { putString(KEY_CURRENT_SOUND, value.name) }
        }
    
    var volume: Float
        get() = prefs.getFloat(KEY_VOLUME, 0.7f)
        set(value) {
            prefs.edit { putFloat(KEY_VOLUME, value) }
        }
    
    var vibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION, true)
        set(value) {
            prefs.edit { putBoolean(KEY_VIBRATION, value) }
        }
    
    var vibrationIntensity: Int
        get() = prefs.getInt(KEY_VIBRATION_INTENSITY, 50)
        set(value) {
            prefs.edit { putInt(KEY_VIBRATION_INTENSITY, value) }
        }
    
    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder().setMaxStreams(10).setAudioAttributes(audioAttributes).build()
        loadSounds(context)
    }
    
    private fun loadSounds(context: Context) {
        soundMap[SoundType.MECHANICAL] = soundPool.load(context, R.raw.sound_mechanical, 1)
        soundMap[SoundType.TYPEWRITER] = soundPool.load(context, R.raw.sound_typewriter, 1)
        soundMap[SoundType.BUBBLE] = soundPool.load(context, R.raw.sound_bubble, 1)
        soundMap[SoundType.WOOD] = soundPool.load(context, R.raw.sound_wood, 1)
        soundMap[SoundType.DIGITAL] = soundPool.load(context, R.raw.sound_digital, 1)
        soundMap[SoundType.SOFT] = soundPool.load(context, R.raw.sound_soft, 1)
    }
    
    fun playKeySound() {
        val soundId = soundMap[currentSound] ?: return
        soundPool.play(soundId, volume, volume, 1, 0, 1.0f)
    }
    
    fun playSpecialSound() {
        val soundId = soundMap[currentSound] ?: return
        soundPool.play(soundId, volume, volume, 1, 0, 1.2f)
    }
    
    fun playDeleteSound() {
        val soundId = soundMap[currentSound] ?: return
        soundPool.play(soundId, volume * 0.8f, volume * 0.8f, 1, 0, 0.8f)
    }
    
    fun playSpaceSound() {
        val soundId = soundMap[currentSound] ?: return
        soundPool.play(soundId, volume * 0.6f, volume * 0.6f, 1, 0, 0.9f)
    }
    
    fun vibrate() {
        if (!vibrationEnabled) return
        val duration = (vibrationIntensity * 0.5).toLong()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
    
    fun release() {
        soundPool.release()
    }
    
    companion object {
        private const val PREFS_NAME = "colorkey_sound"
        private const val KEY_CURRENT_SOUND = "current_sound"
        private const val KEY_VOLUME = "sound_volume"
        private const val KEY_VIBRATION = "vibration_enabled"
        private const val KEY_VIBRATION_INTENSITY = "vibration_intensity"
    }
}
