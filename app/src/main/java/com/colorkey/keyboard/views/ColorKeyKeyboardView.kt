package com.colorkey.keyboard.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.*
import android.view.animation.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.colorkey.keyboard.ColorKeyApp
import com.colorkey.keyboard.R
import com.colorkey.keyboard.themes.KeyboardTheme
import kotlin.math.*

class ColorKeyKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var currentTheme: KeyboardTheme = ColorKeyApp.instance.themeManager.currentTheme
    private var isShifted = false
    private var isSymbols = false
    private var effectContainer: FrameLayout? = null
    private var listener: OnKeyActionListener? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val masterColors = listOf(
        Color.parseColor("#FF6B6B"), Color.parseColor("#4ECDC4"),
        Color.parseColor("#FFE66D"), Color.parseColor("#FF006E"),
        Color.parseColor("#00F5FF"), Color.parseColor("#BF00FF")
    )
    private var colorIndex = 0

    interface OnKeyActionListener {
        fun onKey(code: Int, text: String)
        fun onSpecialKey(code: Int)
    }

    init {
        orientation = VERTICAL
        setPadding(16, 16, 16, 32)
        buildKeyboard()
    }

    fun setEffectContainer(container: FrameLayout) { this.effectContainer = container }
    fun setOnKeyActionListener(listener: OnKeyActionListener) { this.listener = listener }

    fun applyTheme(theme: KeyboardTheme) {
        currentTheme = theme
        setBackgroundColor(theme.keyboardBackgroundColor)
        buildKeyboard()
        invalidate()
    }

    fun toggleShift() { isShifted = !isShifted; buildKeyboard() }
    fun toggleSymbols() { isSymbols = !isSymbols; buildKeyboard() }
    fun toggleEmoji() { listener?.onSpecialKey(-3) }

    private fun buildKeyboard() {
        removeAllViews()
        val rows = if (isSymbols) getSymbolRows() else getLetterRows()
        rows.forEach { row ->
            val rowLayout = LinearLayout(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    setMargins(0, 6.dpToPx(), 0, 0)
                }
            }
            row.forEach { key -> rowLayout.addView(createKeyView(key)) }
            addView(rowLayout)
        }
    }

    private fun createKeyView(key: KeyData): View {
        val textView = androidx.appcompat.widget.AppCompatTextView(context).apply {
            text = if (isShifted && key.shiftText != null) key.shiftText else key.text
            gravity = Gravity.CENTER
            textSize = if (key.isAction) 14f else 20f
            setTextColor(currentTheme.keyTextColor)
            val params = LayoutParams(0, 56.dpToPx(), key.weight).apply {
                setMargins(4.dpToPx(), 0, 4.dpToPx(), 0)
            }
            layoutParams = params
            background = createKeyBackground(key)

            setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        animateKeyPress(view)
                        triggerEffect(event.rawX, event.rawY, view)
                        triggerMasterTheme(view)
                        if (key.code >= 0) listener?.onKey(key.code, text.toString())
                        else listener?.onSpecialKey(key.code)
                        true
                    }
                    else -> false
                }
            }
        }
        return textView
    }

    private fun createKeyBackground(key: KeyData): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = currentTheme.keyCornerRadius
            when {
                key.isAction -> setColor(currentTheme.accentColor)
                key.isSpecial -> {
                    setColor(currentTheme.keyBackgroundColor)
                    val darker = manipulateColor(currentTheme.keyBackgroundColor, 0.8f)
                    setStroke(2.dpToPx(), darker)
                }
                else -> setColor(currentTheme.keyBackgroundColor)
            }
        }
    }

    private fun animateKeyPress(view: View) {
        val prefs = context.getSharedPreferences("colorkey_settings", 0)
        if (!prefs.getBoolean("key_motion_enabled", true)) return

        val scaleDown = ScaleAnimation(1f, 0.85f, 1f, 0.85f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 50; fillAfter = true
        }
        val scaleUp = ScaleAnimation(0.85f, 1f, 0.85f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 150; startOffset = 50; fillAfter = true
            interpolator = OvershootInterpolator(2f)
        }
        val set = AnimationSet(true).apply { addAnimation(scaleDown); addAnimation(scaleUp) }
        view.startAnimation(set)

        if (prefs.getBoolean("key_blend_enabled", true)) {
            val blendIntensity = prefs.getFloat("blend_intensity", 0.5f)
            val originalColor = (view.background as? GradientDrawable)?.color?.defaultColor ?: currentTheme.keyBackgroundColor
            val blendColor = blendColors(originalColor, currentTheme.accentColor, blendIntensity)
            (view.background as? GradientDrawable)?.setColor(blendColor)
            view.postDelayed({ (view.background as? GradientDrawable)?.setColor(originalColor) }, 200)
        }
    }

    private fun triggerEffect(x: Float, y: Float, view: View) {
        effectContainer?.let { container ->
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val viewX = location[0] + view.width / 2f
            val viewY = location[1] + view.height / 2f
            try {
                ColorKeyApp.instance.effectManager.applyEffect(
                    container, viewX - container.x, viewY - container.y, currentTheme.accentColor
                )
            } catch (e: Exception) {}
        }
    }

    private fun triggerMasterTheme(view: View) {
        val prefs = context.getSharedPreferences("colorkey_settings", 0)
        if (!prefs.getBoolean("master_theme_enabled", true)) return

        val color = masterColors[colorIndex % masterColors.size]
        colorIndex++
        val gradient = GradientDrawable(GradientDrawable.Orientation.TL_BR,
            intArrayOf(color, manipulateColor(color, 0.7f))).apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = currentTheme.keyCornerRadius
        }
        view.background = gradient
        view.postDelayed({
            view.background = createKeyBackground(getKeyDataFromView(view) ?: return@postDelayed)
        }, 300)
    }

    private fun getKeyDataFromView(view: View): KeyData? { return null }

    private fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
        val inverseRatio = 1f - ratio
        val a = (Color.alpha(color1) * inverseRatio + Color.alpha(color2) * ratio).toInt()
        val r = (Color.red(color1) * inverseRatio + Color.red(color2) * ratio).toInt()
        val g = (Color.green(color1) * inverseRatio + Color.green(color2) * ratio).toInt()
        val b = (Color.blue(color1) * inverseRatio + Color.blue(color2) * ratio).toInt()
        return Color.argb(a, r, g, b)
    }

    private fun manipulateColor(color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = (Color.red(color) * factor).toInt().coerceIn(0, 255)
        val g = (Color.green(color) * factor).toInt().coerceIn(0, 255)
        val b = (Color.blue(color) * factor).toInt().coerceIn(0, 255)
        return Color.argb(a, r, g, b)
    }

    private fun getLetterRows(): List<List<KeyData>> = listOf(
        listOf(
            KeyData('q'.code, "q", "Q", weight = 1f),
            KeyData('w'.code, "w", "W", weight = 1f),
            KeyData('e'.code, "e", "E", weight = 1f),
            KeyData('r'.code, "r", "R", weight = 1f),
            KeyData('t'.code, "t", "T", weight = 1f),
            KeyData('y'.code, "y", "Y", weight = 1f),
            KeyData('u'.code, "u", "U", weight = 1f),
            KeyData('i'.code, "i", "I", weight = 1f),
            KeyData('o'.code, "o", "O", weight = 1f),
            KeyData('p'.code, "p", "P", weight = 1f)
        ),
        listOf(
            KeyData('a'.code, "a", "A", weight = 1f),
            KeyData('s'.code, "s", "S", weight = 1f),
            KeyData('d'.code, "d", "D", weight = 1f),
            KeyData('f'.code, "f", "F", weight = 1f),
            KeyData('g'.code, "g", "G", weight = 1f),
            KeyData('h'.code, "h", "H", weight = 1f),
            KeyData('j'.code, "j", "J", weight = 1f),
            KeyData('k'.code, "k", "K", weight = 1f),
            KeyData('l'.code, "l", "L", weight = 1f)
        ),
        listOf(
            KeyData(-1, "⇧", null, weight = 1.5f, isSpecial = true),
            KeyData('z'.code, "z", "Z", weight = 1f),
            KeyData('x'.code, "x", "X", weight = 1f),
            KeyData('c'.code, "c", "C", weight = 1f),
            KeyData('v'.code, "v", "V", weight = 1f),
            KeyData('b'.code, "b", "B", weight = 1f),
            KeyData('n'.code, "n", "N", weight = 1f),
            KeyData('m'.code, "m", "M", weight = 1f),
            KeyData(-5, "⌫", null, weight = 1.5f, isSpecial = true)
        ),
        listOf(
            KeyData(-2, "?123", null, weight = 1.5f, isSpecial = true),
            KeyData(-3, "😊", null, weight = 1f, isSpecial = true),
            KeyData(32, " ", null, weight = 4f),
            KeyData(46, ".", null, weight = 1f),
            KeyData(-4, "⏎", null, weight = 1.5f, isAction = true)
        )
    )

    private fun getSymbolRows(): List<List<KeyData>> = listOf(
        listOf(
            KeyData('1'.code, "1", weight = 1f),
            KeyData('2'.code, "2", weight = 1f),
            KeyData('3'.code, "3", weight = 1f),
            KeyData('4'.code, "4", weight = 1f),
            KeyData('5'.code, "5", weight = 1f),
            KeyData('6'.code, "6", weight = 1f),
            KeyData('7'.code, "7", weight = 1f),
            KeyData('8'.code, "8", weight = 1f),
            KeyData('9'.code, "9", weight = 1f),
            KeyData('0'.code, "0", weight = 1f)
        ),
        listOf(
            KeyData('!'.code, "!", weight = 1f),
            KeyData('@'.code, "@", weight = 1f),
            KeyData('#'.code, "#", weight = 1f),
            KeyData('$'.code, "$", weight = 1f),
            KeyData('%'.code, "%", weight = 1f),
            KeyData('^'.code, "^", weight = 1f),
            KeyData('&'.code, "&", weight = 1f),
            KeyData('*'.code, "*", weight = 1f),
            KeyData('('.code, "(", weight = 1f),
            KeyData(')'.code, ")", weight = 1f)
        ),
        listOf(
            KeyData(-1, "⇧", null, weight = 1.5f, isSpecial = true),
            KeyData('-'.code, "-", weight = 1f),
            KeyData('_'.code, "_", weight = 1f),
            KeyData('='.code, "=", weight = 1f),
            KeyData('+'.code, "+", weight = 1f),
            KeyData('['.code, "[", weight = 1f),
            KeyData(']'.code, "]", weight = 1f),
            KeyData('{'.code, "{", weight = 1f),
            KeyData('}'.code, "}", weight = 1f),
            KeyData(-5, "⌫", null, weight = 1.5f, isSpecial = true)
        ),
        listOf(
            KeyData(-2, "ABC", null, weight = 1.5f, isSpecial = true),
            KeyData(-3, "😊", null, weight = 1f, isSpecial = true),
            KeyData(32, " ", null, weight = 4f),
            KeyData(44, ",", null, weight = 1f),
            KeyData(-4, "⏎", null, weight = 1.5f, isAction = true)
        )
    )

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
    private fun Float.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}

data class KeyData(
    val code: Int,
    val text: String,
    val shiftText: String? = null,
    val weight: Float = 1f,
    val isSpecial: Boolean = false,
    val isAction: Boolean = false
)
