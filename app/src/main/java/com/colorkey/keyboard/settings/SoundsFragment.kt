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
import com.colorkey.keyboard.databinding.FragmentSoundsBinding
import com.colorkey.keyboard.sound.SoundType

class SoundsFragment : Fragment() {
    
    private var _binding: FragmentSoundsBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSoundsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSoundList()
        setupSliders()
        setupToggles()
    }
    
    private fun setupSoundList() {
        val sounds = listOf(
            SoundItem(SoundType.MECHANICAL, R.string.sound_mechanical),
            SoundItem(SoundType.TYPEWRITER, R.string.sound_typewriter),
            SoundItem(SoundType.BUBBLE, R.string.sound_bubble),
            SoundItem(SoundType.WOOD, R.string.sound_wood),
            SoundItem(SoundType.DIGITAL, R.string.sound_digital),
            SoundItem(SoundType.SOFT, R.string.sound_soft)
        )
        val adapter = SoundAdapter(sounds) { soundType ->
            ColorKeyApp.instance.soundManager.currentSound = soundType
            ColorKeyApp.instance.soundManager.playKeySound()
            Toast.makeText(context, getString(R.string.toast_sound_applied), Toast.LENGTH_SHORT).show()
        }
        binding.soundsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
        adapter.setSelectedSound(ColorKeyApp.instance.soundManager.currentSound)
    }
    
    private fun setupSliders() {
        val soundManager = ColorKeyApp.instance.soundManager
        binding.volumeSlider.apply {
            progress = (soundManager.volume * 100).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) soundManager.volume = progress / 100f
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        binding.vibrationSlider.apply {
            progress = soundManager.vibrationIntensity
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) soundManager.vibrationIntensity = progress
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) { soundManager.vibrate() }
            })
        }
    }
    
    private fun setupToggles() {
        binding.vibrationSwitch.isChecked = ColorKeyApp.instance.soundManager.vibrationEnabled
        binding.vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            ColorKeyApp.instance.soundManager.vibrationEnabled = isChecked
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class SoundItem(val type: SoundType, val nameRes: Int)
