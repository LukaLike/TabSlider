package io.github.lukalike.tabslider.util

import android.graphics.Paint
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import io.github.lukalike.tabslider.util.DefaultSliderValues.TEXT_SIZE

fun initPaint(color: Color, density: Float) =
    Paint().apply {
        setColor(color.toArgb())
        textSize = TEXT_SIZE * density
    }

fun Canvas.drawFinalText(text: String, x: Float, y: Float, paint: Paint) =
    nativeCanvas.drawText(text, 0, text.length, x, y, paint)
