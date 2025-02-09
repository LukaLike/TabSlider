package io.github.lukalike.tabslider.util

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.lukalike.tabslider.data.SliderSettings
import io.github.lukalike.tabslider.data.TabSettings
import io.github.lukalike.tabslider.data.TabSliderColors

object TabSliderDefaults {

    fun colors() =
        TabSliderColors(
            sliderColor = DefaultSliderValues.Blue,
            sliderTextColor = Color.White,
            collapsedTabColor = DefaultSliderValues.DarkBlue,
            expandedTabColor = DefaultSliderValues.TransparentBlue,
            tabTextColor = Color.White,
            backgroundColor = Color.LightGray,
        )

    fun sliderSettings() =
        SliderSettings(
            height = DefaultSliderValues.DEFAULT_TAB_SIZE,
            cornerRadius = CornerRadius(DefaultSliderValues.DEFAULT_SLIDER_CORNER_RADIUS),
            startText = null,
            endText = null
        )

    fun tabSettings() =
        TabSettings(
            label = "",
            expandedSizeMultiplier = DefaultSliderValues.DEFAULT_EXPANDED_SIZE_MODIFIER,
            expansionSpeed = DefaultSliderValues.DEFAULT_ANIMATION_DURATION,
            cornerRadius = CornerRadius(DefaultSliderValues.DEFAULT_TAB_CORNER_RADIUS)
        )

}

internal object DefaultSliderValues {

    val Blue = Color(0xFF044372)
    val DarkBlue = Color(0xFF03243D)
    val TransparentBlue = Color(0xCC03243D)

    val DEFAULT_TAB_SIZE = 40.dp

    const val DEFAULT_EXPANDED_SIZE_MODIFIER = 1.65F
    const val DEFAULT_ANIMATION_DURATION = 300

    const val DEFAULT_SLIDER_CORNER_RADIUS = 2F
    const val DEFAULT_TAB_CORNER_RADIUS = 2F

    const val LABEL_TEXT_OFFSET = 0F
    const val TEXT_OFFSET = 8F

    const val TEXT_SIZE = 12F

}
