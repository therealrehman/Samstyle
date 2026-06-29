package com.colorkey.keyboard.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.colorkey.keyboard.ColorKeyApp
import com.colorkey.keyboard.R
import com.colorkey.keyboard.databinding.FragmentEffectsBinding
import com.colorkey.keyboard.effects.EffectType
import com.colorkey.keyboard.effects.KeyEffect

class EffectsFragment : Fragment() {
    
    private var _binding: FragmentEffectsBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEffectsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEffectList()
        setupSliders()
        setupToggles()
    }
    
    private fun setupEffectList() {
        val effects = listOf(
            EffectItem(EffectType.NONE, R.string.effect_none, R.drawable.key_background),
            EffectItem(EffectType.RIPPLE, R.string.effect_ripple, R.drawable.key_background),
            EffectItem(EffectType.PARTICLE, R.string.effect_particle, R.drawable.key_background),
            EffectItem(EffectType.RADAR, R.string.effect_radar, R.drawable.key_background),
            EffectItem(EffectType.BLOOM, R.string.effect_bloom, R.drawable.key_background),
            EffectItem(EffectType.SHOCKWAVE, R.string.effect_shockwave, R.drawable.key_background)
        )
        val adapter = EffectAdapter(effects) { effectType ->
            val effect = KeyEffect(effectType, binding.intensitySlider.progress / 100f)
            ColorKeyApp.instance.effectManager.currentEffect = effect
            Toast.makeText(context, getString(R.string.toast_effect_applied), Toast.LENGTH_SHORT).show()
        }
        binding.effectsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
        val current = ColorKeyApp.instance.effectManager.currentEffect
        adapter.setSelectedEffect(current.type)
    }
    
    private fun setupSliders() {
        val effectManager = ColorKeyApp.instance.effectManager
        binding.intensitySlider.apply {
            progress = (effectManager.getIntensity() * 100).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        val effect = effectManager.currentEffect
                        effectManager.currentEffect = effect.copy(intensity = progress / 100f)
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        binding.blendIntensitySlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) saveBlendIntensity(progress / 100f)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
    
    private fun setupToggles() {
        val prefs = requireContext().getSharedPreferences("colorkey_settings", 0)
        binding.keyBlendSwitch.isChecked = prefs.getBoolean("key_blend_enabled", true)
        binding.keyBlendSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("key_blend_enabled", isChecked).apply()
        }
        binding.boardBlendSwitch.isChecked = prefs.getBoolean("board_blend_enabled", false)
        binding.boardBlendSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("board_blend_enabled", isChecked).apply()
        }
        binding.keyMotionSwitch.isChecked = prefs.getBoolean("key_motion_enabled", true)
        binding.keyMotionSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("key_motion_enabled", isChecked).apply()
        }
    }
    
    private fun saveBlendIntensity(intensity: Float) {
        requireContext().getSharedPreferences("colorkey_settings", 0)
            .edit().putFloat("blend_intensity", intensity).apply()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class EffectItem(val type: EffectType, val nameRes: Int, val iconRes: Int)
