package com.colorkey.keyboard.effects

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.View
import android.view.animation.*
import android.widget.FrameLayout
import androidx.core.content.edit
import com.colorkey.keyboard.R
import java.util.*
import kotlin.math.*

class EffectManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val random = Random()
    
    var currentEffect: KeyEffect
        get() {
            val id = prefs.getString(KEY_CURRENT_EFFECT, EffectType.RIPPLE.name) ?: EffectType.RIPPLE.name
            return KeyEffect(EffectType.valueOf(id), getIntensity())
        }
        set(value) {
            prefs.edit {
                putString(KEY_CURRENT_EFFECT, value.type.name)
                putFloat(KEY_INTENSITY, value.intensity)
            }
        }
    
    fun getIntensity(): Float = prefs.getFloat(KEY_INTENSITY, 0.7f)
    
    fun applyEffect(container: FrameLayout, x: Float, y: Float, color: Int, effect: KeyEffect = currentEffect) {
        when (effect.type) {
            EffectType.NONE -> {}
            EffectType.RIPPLE -> createRipple(container, x, y, color, effect.intensity)
            EffectType.PARTICLE -> createParticles(container, x, y, color, effect.intensity)
            EffectType.RADAR -> createRadar(container, x, y, color, effect.intensity)
            EffectType.BLOOM -> createBloom(container, x, y, color, effect.intensity)
            EffectType.SHOCKWAVE -> createShockwave(container, x, y, color, effect.intensity)
        }
    }
    
    private fun createRipple(container: FrameLayout, x: Float, y: Float, color: Int, intensity: Float) {
        val ripple = View(container.context).apply {
            background = android.graphics.drawable.GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.OVAL
                setColor(color and 0x40FFFFFF)
            }
            layoutParams = FrameLayout.LayoutParams(20, 20).apply {
                leftMargin = (x - 10).toInt()
                topMargin = (y - 10).toInt()
            }
        }
        container.addView(ripple)
        val maxSize = (100 * intensity).toInt()
        val anim = ScaleAnimation(1f, maxSize / 20f, 1f, maxSize / 20f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = (600 / intensity).toLong()
            interpolator = DecelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(a: Animation?) {}
                override fun onAnimationRepeat(a: Animation?) {}
                override fun onAnimationEnd(a: Animation?) { container.removeView(ripple) }
            })
        }
        val fadeAnim = AlphaAnimation(0.6f, 0f).apply { duration = (600 / intensity).toLong() }
        ripple.startAnimation(anim)
        ripple.startAnimation(fadeAnim)
    }
    
    private fun createParticles(container: FrameLayout, x: Float, y: Float, color: Int, intensity: Float) {
        val particleCount = (8 * intensity).toInt().coerceIn(3, 20)
        repeat(particleCount) {
            val particle = View(container.context).apply {
                background = android.graphics.drawable.GradientDrawable().apply {
                    shape = android.graphics.drawable.GradientDrawable.OVAL
                    setColor(color)
                }
                val size = (4 + random.nextInt(8)).dpToPx(container.context)
                layoutParams = FrameLayout.LayoutParams(size, size).apply {
                    leftMargin = (x - size / 2).toInt()
                    topMargin = (y - size / 2).toInt()
                }
            }
            container.addView(particle)
            val angle = random.nextFloat() * 2 * PI
            val distance = (50 + random.nextInt(100)) * intensity
            val tx = (cos(angle) * distance).toFloat()
            val ty = (sin(angle) * distance).toFloat()
            val animX = android.animation.ObjectAnimator.ofFloat(particle, "translationX", 0f, tx)
            val animY = android.animation.ObjectAnimator.ofFloat(particle, "translationY", 0f, ty)
            val animAlpha = android.animation.ObjectAnimator.ofFloat(particle, "alpha", 1f, 0f)
            val animScale = android.animation.ObjectAnimator.ofFloat(particle, "scaleX", 1f, 0f)
            val animScaleY = android.animation.ObjectAnimator.ofFloat(particle, "scaleY", 1f, 0f)
            android.animation.AnimatorSet().apply {
                playTogether(animX, animY, animAlpha, animScale, animScaleY)
                duration = (400 + random.nextInt(400)).toLong()
                addListener(object : android.animation.AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: android.animation.Animator) {
                        container.removeView(particle)
                    }
                })
                start()
            }
        }
    }
    
    private fun createRadar(container: FrameLayout, x: Float, y: Float, color: Int, intensity: Float) {
        val radar = View(container.context).apply {
            background = android.graphics.drawable.GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.OVAL
                setStroke((3 * intensity).toInt(), color)
                setColor(Color.TRANSPARENT)
            }
            layoutParams = FrameLayout.LayoutParams(20, 20).apply {
                leftMargin = (x - 10).toInt()
                topMargin = (y - 10).toInt()
            }
        }
        container.addView(radar)
        val maxSize = (120 * intensity).toInt()
        val scaleAnim = ScaleAnimation(1f, maxSize / 20f, 1f, maxSize / 20f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = (800 / intensity).toLong()
            interpolator = AccelerateDecelerateInterpolator()
        }
        val alphaAnim = AlphaAnimation(0.8f, 0f).apply {
            duration = (800 / intensity).toLong()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(a: Animation?) {}
                override fun onAnimationRepeat(a: Animation?) {}
                override fun onAnimationEnd(a: Animation?) { container.removeView(radar) }
            })
        }
        val set = AnimationSet(true).apply { addAnimation(scaleAnim); addAnimation(alphaAnim) }
        radar.startAnimation(set)
    }
    
    private fun createBloom(container: FrameLayout, x: Float, y: Float, color: Int, intensity: Float) {
        val layers = 3
        repeat(layers) { layer ->
            val bloom = View(container.context).apply {
                background = android.graphics.drawable.GradientDrawable().apply {
                    shape = android.graphics.drawable.GradientDrawable.OVAL
                    val alpha = (80 / (layer + 1))
                    setColor((color and 0x00FFFFFF) or (alpha shl 24))
                }
                val size = (30 + layer * 20).dpToPx(context)
                layoutParams = FrameLayout.LayoutParams(size, size).apply {
                    leftMargin = (x - size / 2).toInt()
                    topMargin = (y - size / 2).toInt()
                }
            }
            container.addView(bloom)
            val maxScale = 2f + layer * 0.5f
            val scaleAnim = ScaleAnimation(0.5f, maxScale, 0.5f, maxScale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
                duration = (500 + layer * 100).toLong()
                interpolator = DecelerateInterpolator()
            }
            val alphaAnim = AlphaAnimation(0.6f / (layer + 1), 0f).apply {
                duration = (500 + layer * 100).toLong()
                startOffset = (layer * 50).toLong()
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(a: Animation?) {}
                    override fun onAnimationRepeat(a: Animation?) {}
                    override fun onAnimationEnd(a: Animation?) { container.removeView(bloom) }
                })
            }
            val set = AnimationSet(true).apply { addAnimation(scaleAnim); addAnimation(alphaAnim) }
            bloom.startAnimation(set)
        }
    }
    
    private fun createShockwave(container: FrameLayout, x: Float, y: Float, color: Int, intensity: Float) {
        val waveCount = 2
        repeat(waveCount) { wave ->
            val waveView = View(container.context).apply {
                background = android.graphics.drawable.GradientDrawable().apply {
                    shape = android.graphics.drawable.GradientDrawable.OVAL
                    setStroke((2 * intensity).toInt(), color)
                    setColor(Color.TRANSPARENT)
                }
                layoutParams = FrameLayout.LayoutParams(20, 20).apply {
                    leftMargin = (x - 10).toInt()
                    topMargin = (y - 10).toInt()
                }
            }
            container.addView(waveView)
            val maxSize = (100 + wave * 40) * intensity
            val scaleAnim = ScaleAnimation(1f, maxSize / 20f, 1f, maxSize / 20f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
                duration = (600 / intensity).toLong()
                startOffset = (wave * 150).toLong()
                interpolator = AccelerateInterpolator()
            }
            val alphaAnim = AlphaAnimation(0.7f, 0f).apply {
                duration = (600 / intensity).toLong()
                startOffset = (wave * 150).toLong()
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(a: Animation?) {}
                    override fun onAnimationRepeat(a: Animation?) {}
                    override fun onAnimationEnd(a: Animation?) { container.removeView(waveView) }
                })
            }
            val set = AnimationSet(true).apply { addAnimation(scaleAnim); addAnimation(alphaAnim) }
            waveView.startAnimation(set)
        }
    }
    
    private fun Int.dpToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
    
    companion object {
        private const val PREFS_NAME = "colorkey_effects"
        private const val KEY_CURRENT_EFFECT = "current_effect"
        private const val KEY_INTENSITY = "effect_intensity"
    }
}
