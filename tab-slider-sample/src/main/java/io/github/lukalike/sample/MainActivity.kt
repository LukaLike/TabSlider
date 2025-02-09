package io.github.lukalike.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.lukalike.sample.ui.theme.TabSliderTheme
import io.github.lukalike.tabslider.HorizontalTabSlider
import io.github.lukalike.tabslider.VerticalTabSlider
import io.github.lukalike.tabslider.util.TabSliderDefaults
import kotlin.math.ceil
import kotlin.math.floor

private val horizontalRange = 0f..10f
private val verticalRange = 0f..300f

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TabSliderTheme {
                TabSlidersView()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TabSlidersView() {
    var horizontalValue by remember { mutableFloatStateOf(.4F) }
    var verticalValue by remember { mutableFloatStateOf(.4F) }

    Column(
        modifier = Modifier
            .background(White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        HorizontalTabSlider(
            value = (horizontalValue * 10).toInt().toFloat(),
            onValueChange = { horizontalValue = it },
            valueRange = horizontalRange,
            sliderSettings = TabSliderDefaults.sliderSettings().copy(
                startText = floor(horizontalRange.start).toInt().toString(),
                endText = ceil(horizontalRange.endInclusive).toInt().toString()
            ),
            tabSettings = TabSliderDefaults.tabSettings()
                .copy(label = getPosText(horizontalValue))
        )

        VerticalTabSlider(
            value = (verticalValue * 10).toInt().toFloat(),
            onValueChange = { verticalValue = it },
            valueRange = verticalRange,
            colors = TabSliderDefaults.colors().copy(
                sliderColor = Black,
                collapsedTabColor = LightGray,
                expandedTabColor = LightGray,
                tabTextColor = Black,
                backgroundColor = LightGray.copy(alpha = .2f)
            ),
            sliderSettings = TabSliderDefaults.sliderSettings().copy(
                startText = floor(verticalRange.start).toInt().toString(),
                endText = ceil(verticalRange.endInclusive).toInt().toString()
            ),
            tabSettings = TabSliderDefaults.tabSettings()
                .copy(
                    label = getInvertedPosText(verticalValue),
                    expandedSizeMultiplier = 2f,
                    expansionSpeed = 100
                )
        )
    }
}

private fun getPosText(currentPos: Float): String {
    val actualPos =
        horizontalRange.start + (horizontalRange.endInclusive - horizontalRange.start) * currentPos
    return actualPos.toInt().toString()
}

private fun getInvertedPosText(currentPos: Float): String {
    val actualPos =
        verticalRange.start + (verticalRange.endInclusive - verticalRange.start) * (1 - currentPos)
    return actualPos.toInt().toString()
}

@ExperimentalComposeUiApi
@Preview(apiLevel = 34, showBackground = true)
@Composable
fun TabSliderPreview() {
    TabSliderTheme {
        TabSlidersView()
    }
}
