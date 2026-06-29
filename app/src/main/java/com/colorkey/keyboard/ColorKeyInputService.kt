package com.colorkey.keyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.colorkey.keyboard.themes.KeyboardTheme
import com.colorkey.keyboard.views.ColorKeyKeyboardView

class ColorKeyInputService : InputMethodService() {
    
    private lateinit var keyboardView: ColorKeyKeyboardView
    private lateinit var effectContainer: FrameLayout
    private var currentTheme: KeyboardTheme? = null
    
    override fun onCreate() {
        super.onCreate()
        updateTheme()
    }
    
    override fun onCreateInputView(): View {
        val rootView = layoutInflater.inflate(R.layout.keyboard_input, null) as FrameLayout
        effectContainer = rootView.findViewById(R.id.effectContainer)
        keyboardView = rootView.findViewById(R.id.keyboardView)
        
        keyboardView.setEffectContainer(effectContainer)
        keyboardView.setOnKeyActionListener(object : ColorKeyKeyboardView.OnKeyActionListener {
            override fun onKey(code: Int, text: String) {
                handleKey(code, text)
            }
            override fun onSpecialKey(code: Int) {
                handleSpecialKey(code)
            }
        })
        
        applyCurrentTheme()
        return rootView
    }
    
    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        updateTheme()
        applyCurrentTheme()
    }
    
    private fun updateTheme() {
        currentTheme = try {
            ColorKeyApp.instance.themeManager.currentTheme
        } catch (e: Exception) {
            null
        }
    }
    
    private fun applyCurrentTheme() {
        currentTheme?.let { theme ->
            keyboardView.applyTheme(theme)
        }
    }
    
    private fun handleKey(code: Int, text: String) {
        val ic = currentInputConnection ?: return
        when (code) {
            -5 -> ic.deleteSurroundingText(1, 0)
            -1 -> keyboardView.toggleShift()
            -2 -> keyboardView.toggleSymbols()
            -3 -> keyboardView.toggleEmoji()
            -4 -> sendKeyChar('\n')
            32 -> {
                ic.commitText(" ", 1)
                playSpaceSound()
            }
            else -> {
                ic.commitText(text, 1)
                playKeySound()
            }
        }
    }
    
    private fun handleSpecialKey(code: Int) {
        when (code) {
            -10 -> requestHideSelf(0)
        }
    }
    
    private fun playKeySound() {
        try {
            ColorKeyApp.instance.soundManager.playKeySound()
            ColorKeyApp.instance.soundManager.vibrate()
        } catch (e: Exception) {}
    }
    
    private fun playSpaceSound() {
        try {
            ColorKeyApp.instance.soundManager.playSpaceSound()
            ColorKeyApp.instance.soundManager.vibrate()
        } catch (e: Exception) {}
    }
    
    override fun onDestroy() {
        super.onDestroy()
        try {
            ColorKeyApp.instance.soundManager.release()
        } catch (e: Exception) {}
    }
}
