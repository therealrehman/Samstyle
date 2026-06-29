package com.colorkey.keyboard.utils

import android.content.Context
import android.util.TypedValue

fun Int.dpToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
fun Float.dpToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
fun Int.spToPx(context: Context): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context.resources.displayMetrics)
