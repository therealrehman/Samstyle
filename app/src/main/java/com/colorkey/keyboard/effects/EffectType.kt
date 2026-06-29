package com.colorkey.keyboard.effects

enum class EffectType {
    NONE, RIPPLE, PARTICLE, RADAR, BLOOM, SHOCKWAVE
}

data class KeyEffect(
    val type: EffectType,
    val intensity: Float = 0.7f
)
