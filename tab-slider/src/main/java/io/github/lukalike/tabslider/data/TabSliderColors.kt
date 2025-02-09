package io.github.lukalike.tabslider.data

import androidx.compose.ui.graphics.Color

/**
 * Tab slider colors
 *
 * @param sliderColor color of the slider
 * @param sliderTextColor text color of the slider
 * @param collapsedTabColor color of the collapsed tab
 * @param expandedTabColor color of the expanded tab
 * @param tabTextColor text color of the tab
 * @param backgroundColor color of the area that slider and tab could cover
 */
data class TabSliderColors(
    val sliderColor: Color,
    val sliderTextColor: Color,
    val collapsedTabColor: Color,
    val expandedTabColor: Color,
    val tabTextColor: Color,
    val backgroundColor: Color
)
