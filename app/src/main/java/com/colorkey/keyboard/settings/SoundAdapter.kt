package com.colorkey.keyboard.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.colorkey.keyboard.R
import com.colorkey.keyboard.sound.SoundType

class SoundAdapter(
    private val sounds: List<SoundItem>,
    private val onSoundClick: (SoundType) -> Unit
) : RecyclerView.Adapter<SoundAdapter.SoundViewHolder>() {
    
    private var selectedSound: SoundType = SoundType.MECHANICAL
    
    fun setSelectedSound(type: SoundType) {
        selectedSound = type
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sound, parent, false)
        return SoundViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        val sound = sounds[position]
        holder.bind(sound, sound.type == selectedSound)
    }
    
    override fun getItemCount(): Int = sounds.size
    
    inner class SoundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.soundCard)
        private val iconView: ImageView = itemView.findViewById(R.id.soundIcon)
        private val nameText: TextView = itemView.findViewById(R.id.soundName)
        private val playIcon: ImageView = itemView.findViewById(R.id.playIcon)
        
        fun bind(sound: SoundItem, isSelected: Boolean) {
            nameText.text = itemView.context.getString(sound.nameRes)
            if (isSelected) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primary))
                nameText.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_light))
                playIcon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.text_light))
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.bg_card))
                nameText.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_primary))
                playIcon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.text_secondary))
            }
            itemView.setOnClickListener {
                onSoundClick(sound.type)
                setSelectedSound(sound.type)
            }
        }
    }
}
