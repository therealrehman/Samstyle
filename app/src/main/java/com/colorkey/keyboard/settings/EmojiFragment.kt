package com.colorkey.keyboard.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.colorkey.keyboard.databinding.FragmentEmojiBinding

class EmojiFragment : Fragment() {
    
    private var _binding: FragmentEmojiBinding? = null
    private val binding get() = _binding!!
    
    private val emojis = listOf(
        "😀", "😂", "🥰", "😎", "🤔", "😭", "😡", "🥳",
        "👍", "👎", "👏", "🙏", "💪", "🎉", "🔥", "❤️",
        "😴", "🤯", "🥶", "🤠", "👻", "👽", "🤖", "💩",
        "🐶", "🐱", "🦁", "🐯", "🐨", "🐼", "🐸", "🐙",
        "🌸", "🌺", "🌹", "🌻", "🌲", "🌵", "🍁", "🍄",
        "🍎", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓", "🫐",
        "⚽", "🏀", "🏈", "⚾", "🎾", "🏐", "🏉", "🎱",
        "🚗", "🚕", "🚙", "🚌", "🚎", "🏎️", "🚓", "🚑",
        "⌚", "📱", "💻", "⌨️", "🖥️", "🖨️", "🖱️", "🖲️",
        "🎵", "🎶", "🎼", "🎸", "🎹", "🎺", "🎻", "🥁",
        "🌞", "🌝", "🌚", "🌑", "🌒", "🌓", "🌔", "🌕",
        "⭐", "🌟", "✨", "⚡", "🔥", "💥", "☄️", "☀️"
    )
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEmojiBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = EmojiAdapter(emojis) { emoji ->
            val clipboard = requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("emoji", emoji)
            clipboard.setPrimaryClip(clip)
            android.widget.Toast.makeText(context, "Copied: $emoji", android.widget.Toast.LENGTH_SHORT).show()
        }
        binding.emojiRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 8)
            this.adapter = adapter
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
