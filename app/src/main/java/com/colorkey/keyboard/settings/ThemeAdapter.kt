package com.colorkey.keyboard.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.colorkey.keyboard.R
import com.colorkey.keyboard.themes.KeyboardTheme

class ThemeAdapter(
    private val themes: List<KeyboardTheme>,
    private val onThemeClick: (KeyboardTheme) -> Unit
) : RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>() {
    
    private var selectedThemeId: String = ""
    
    fun setSelectedTheme(id: String) {
        selectedThemeId = id
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false)
        return ThemeViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val theme = themes[position]
        holder.bind(theme, theme.id == selectedThemeId)
    }
    
    override fun getItemCount(): Int = themes.size
    
    inner class ThemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.themeCard)
        private val previewView: View = itemView.findViewById(R.id.themePreview)
        private val nameText: TextView = itemView.findViewById(R.id.themeName)
        private val checkMark: ImageView = itemView.findViewById(R.id.checkMark)
        
        fun bind(theme: KeyboardTheme, isSelected: Boolean) {
            previewView.background = theme.createPreviewDrawable()
            nameText.text = itemView.context.getString(theme.nameRes)
            if (isSelected) {
                cardView.strokeWidth = 4
                cardView.strokeColor = ContextCompat.getColor(itemView.context, R.color.primary)
                checkMark.visibility = View.VISIBLE
            } else {
                cardView.strokeWidth = 0
                checkMark.visibility = View.GONE
            }
            itemView.setOnClickListener { onThemeClick(theme) }
        }
    }
}
