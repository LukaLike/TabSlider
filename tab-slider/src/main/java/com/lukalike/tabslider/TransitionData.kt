package com.lukalike.tabslider

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

/**
 * TabSlider's transition data
 *
 * @constructor create TabSlider's transition data object
 *
 * @param size the size of the tab
 * @param offset the offset of the tab
 * @param color the color of the tab
 */
class TransitionData(size: State<Size>, offset: State<Offset>, color: State<Color>) {
    val size by size
    val offset by offset
    val color by color
}