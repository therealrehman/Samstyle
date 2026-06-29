package com.colorkey.keyboard.emoji

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.colorkey.keyboard.databinding.ActivityEmojiPickerBinding

class EmojiPickerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmojiPickerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmojiPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
