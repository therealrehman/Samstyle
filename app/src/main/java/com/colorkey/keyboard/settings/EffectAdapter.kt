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
import com.colorkey.keyboard.effects.EffectType

class EffectAdapter(
    private val effects: List<EffectItem>,
    private val onEffectClick: (EffectType) -> Unit
) : RecyclerView.Adapter<EffectAdapter.EffectViewHolder>() {
    
    private var selectedEffect: EffectType = EffectType.RIPPLE
    
    fun setSelectedEffect(type: EffectType) {
        selectedEffect = type
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EffectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_effect, parent, false)
        return EffectViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: EffectViewHolder, position: Int) {
        val effect = effects[position]
        holder.bind(effect, effect.type == selectedEffect)
    }
    
    override fun getItemCount(): Int = effects.size
    
    inner class EffectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.effectCard)
        private val iconView: ImageView = itemView.findViewById(R.id.effectIcon)
        private val nameText: TextView = itemView.findViewById(R.id.effectName)
        
        fun bind(effect: EffectItem, isSelected: Boolean) {
            nameText.text = itemView.context.getString(effect.nameRes)
            iconView.setImageResource(effect.iconRes)
            if (isSelected) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primary))
                nameText.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_light))
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.bg_card))
                nameText.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_primary))
            }
            itemView.setOnClickListener {
                onEffectClick(effect.type)
                setSelectedEffect(effect.type)
            }
        }
    }
}
