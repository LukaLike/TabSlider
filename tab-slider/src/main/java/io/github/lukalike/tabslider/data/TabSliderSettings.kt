package io.github.lukalike.tabslider.data

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.Dp

/**
 * Tab slider settings
 *
 * @param height height of the slider
 * @param cornerRadius corner radius of the slider
 * @param startText text of the slider left side (start)
 * @param endText text of the slider right side (end)
 */
data class SliderSettings(
    val height: Dp,
    val cornerRadius: CornerRadius,
    val startText: String?,
    val endText: String?
)

/**
 * Tab slider settings
 *
 * @param expandedSizeMultiplier tab expanded size multiplier
 * @param expansionSpeed tab expansion speed
 * @param cornerRadius tab corner radius
 */
data class TabSettings(
    val label: String,
    val expandedSizeMultiplier: Float,
    val expansionSpeed: Int,
    val cornerRadius: CornerRadius
)
