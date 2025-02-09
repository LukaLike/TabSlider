package io.github.lukalike.tabslider

import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.animateSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import io.github.lukalike.tabslider.data.SliderSettings
import io.github.lukalike.tabslider.data.TabSettings
import io.github.lukalike.tabslider.data.TabSliderColors
import io.github.lukalike.tabslider.data.TabToggleState
import io.github.lukalike.tabslider.data.TransitionData
import io.github.lukalike.tabslider.util.DefaultSliderValues.LABEL_TEXT_OFFSET
import io.github.lukalike.tabslider.util.DefaultSliderValues.TEXT_OFFSET
import io.github.lukalike.tabslider.util.TabSliderDefaults
import io.github.lukalike.tabslider.util.drawFinalText
import io.github.lukalike.tabslider.util.initPaint
import io.github.lukalike.tabslider.util.onUpdate
import kotlin.math.max
import kotlin.math.min

/**
 * Horizontal tab slider
 * @param value current value indication of the tab in respect to the main track
 * @param onValueChange value update callback
 * @param modifier modifier of the TabSlider
 * @param enabled controls enabled state of the TabSlider. Applying false will make component
 * not responsive to any actions, also applies different visual colors specified in [colors]
 * @param valueRange range of values of the TabSlider
 * @param onValueChangeFinished lambda function to be invoked when tab value change has ended
 * @param colors [TabSliderColors] that will be used to resolve the colors used in
 * TabSlider. See [TabSliderDefaults.colors].
 * @param sliderSettings [SliderSettings] that will be used to resolve slider settings.
 * See [TabSliderDefaults.sliderSettings].
 * @param tabSettings [TabSettings] that will be used to resolve tab settings.
 * See [TabSliderDefaults.tabSettings].
 * @param interactionSource the [MutableInteractionSource] allowing to listen to interaction changes
 * in TabSlider
 */
@Composable
fun VerticalTabSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: TabSliderColors = TabSliderDefaults.colors(),
    sliderSettings: SliderSettings = TabSliderDefaults.sliderSettings(),
    tabSettings: TabSettings = TabSliderDefaults.tabSettings(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val density = LocalContext.current.resources.displayMetrics.density

    val innerPadding =
        remember { (sliderSettings.height * tabSettings.expandedSizeMultiplier) - sliderSettings.height }
    var currentPosition by remember { mutableFloatStateOf(max(0F, min(1F, value))) }

    val mainRectangle = RectF()
    val movableRectangle = RectF()

    var tabState by remember { mutableStateOf(TabToggleState.Collapsed) }
    val transitionData = updateTransitionData(tabState, colors, sliderSettings, tabSettings)

    Column(
        modifier = modifier
            .width(sliderSettings.height * tabSettings.expandedSizeMultiplier)
            .background(colors.backgroundColor)
            .padding(start = innerPadding)
            .clickable(interactionSource = interactionSource, indication = null, onClick = { })
    ) {
        Canvas(
            modifier = Modifier
                .width(sliderSettings.height)
                .fillMaxHeight()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        onUpdate(
                            calculateCurrentPosition = { input: PointerInputChange ->
                                calculateCurrentPosition(input, mainRectangle, movableRectangle)
                            },
                            onValueChangeFinished = onValueChangeFinished,
                            onValueChange = { newPos, newTabState ->
                                newPos?.let { currentPosition = it }
                                tabState = newTabState
                            }
                        )
                    }
                }
                .onSizeChanged {
                    onSizeChanged(it, mainRectangle, movableRectangle)
                },
            onDraw = {
                // Draw main rectangle
                drawRoundRect(
                    color = colors.sliderColor,
                    cornerRadius = sliderSettings.cornerRadius
                )

                // Draw start & end texts
                drawIntoCanvas {
                    it.drawText(
                        sliderSettings.startText ?: valueRange.start.toString(),
                        mainRectangle,
                        colors.sliderTextColor,
                        Paint.Align.LEFT,
                        sliderSettings.height.value * density / 2.0F,
                        density,
                        mainRectangle.centerX()
                    )
                }
                drawIntoCanvas {
                    it.drawText(
                        sliderSettings.endText ?: valueRange.endInclusive.toString(),
                        mainRectangle,
                        colors.sliderTextColor,
                        Paint.Align.RIGHT,
                        sliderSettings.height.value * density / 2.0F,
                        density,
                        mainRectangle.centerX()
                    )
                }

                // Draw tab
                drawRoundRect(
                    color = transitionData.color,
                    topLeft = Offset(
                        sliderSettings.height.toPx(),
                        getPositionValue(currentPosition, mainRectangle, movableRectangle)
                    ),
                    cornerRadius = tabSettings.cornerRadius,
                    size = Size(-transitionData.size.width, transitionData.size.height)
                )

                // Update tab position
                movableRectangle.offsetTo(
                    transitionData.offset.x,
                    getPositionValue(currentPosition, mainRectangle, movableRectangle)
                )

                // Draw label
                drawIntoCanvas {
                    // Calculate appropriate position for the text of the tab
                    val expandedSize =
                        mainRectangle.width() * tabSettings.expandedSizeMultiplier - mainRectangle.width()
                    val temp = expandedSize / 2 + (mainRectangle.width() / 2 - expandedSize)

                    val pctFromExpanded = (transitionData.size.width - mainRectangle.width()) /
                            (mainRectangle.width() * tabSettings.expandedSizeMultiplier - mainRectangle.width())
                    val currentPct = temp * pctFromExpanded

                    val x =
                        (mainRectangle.width() / 2 - (transitionData.size.width + currentPct - mainRectangle.width()))

                    if (tabState == TabToggleState.Collapsed) {
                        it.drawText(
                            tabSettings.label, movableRectangle, colors.tabTextColor,
                            Paint.Align.CENTER, LABEL_TEXT_OFFSET, density, x
                        )
                    } else if (tabState == TabToggleState.Expanded) {
                        it.drawText(
                            tabSettings.label, movableRectangle, colors.tabTextColor,
                            Paint.Align.CENTER, LABEL_TEXT_OFFSET, density, x
                        )
                    }
                }
            }
        )
    }

    LaunchedEffect(currentPosition) {
        onValueChange(currentPosition)
    }
}

@Composable
private fun updateTransitionData(
    tabState: TabToggleState,
    colors: TabSliderColors,
    sliderSettings: SliderSettings,
    tabSettings: TabSettings
): TransitionData {
    val transition = updateTransition(tabState, label = "Transition")
    val tabSize = with(LocalDensity.current) { sliderSettings.height.toPx() }

    val size = transition.animateSize(
        transitionSpec = { tween(durationMillis = tabSettings.expansionSpeed) },
        label = "Offset"
    ) { state ->
        when (state) {
            TabToggleState.Collapsed -> Size(tabSize, tabSize)
            TabToggleState.Expanded -> Size(tabSize * tabSettings.expandedSizeMultiplier, tabSize)
        }
    }
    val offset = transition.animateOffset(
        transitionSpec = { tween(durationMillis = tabSettings.expansionSpeed) },
        label = "Offset"
    ) { state ->
        when (state) {
            TabToggleState.Collapsed -> Offset(0F, 0F)
            TabToggleState.Expanded -> Offset(
                -(tabSize * tabSettings.expandedSizeMultiplier - tabSize),
                0F
            )
        }
    }
    val color = transition.animateColor(
        transitionSpec = { tween(durationMillis = tabSettings.expansionSpeed) },
        label = "Color"
    ) { state ->
        when (state) {
            TabToggleState.Collapsed -> colors.collapsedTabColor
            TabToggleState.Expanded -> colors.expandedTabColor
        }
    }

    return remember(transition) { TransitionData(size, offset, color) }
}

private fun calculateCurrentPosition(inputChange: PointerInputChange, mainRect: RectF, tab: RectF) =
    max(
        0F,
        min(1F, (inputChange.position.y - (tab.height() / 2)) / (mainRect.height() - tab.height()))
    )

private fun onSizeChanged(intSize: IntSize, mainRect: RectF, tab: RectF) {
    mainRect.set(0F, 0F, intSize.width.toFloat(), intSize.height.toFloat())
    tab.set(0F, 0F, intSize.width.toFloat(), intSize.width.toFloat())
}

private fun Canvas.drawText(
    text: String, rect: RectF,
    color: Color, align: Paint.Align,
    offset: Float, density: Float, x: Float,
) {
    val paint = initPaint(color, density)

    val updatedOffset = if (paint.measureText(text) > rect.width()) {
        TEXT_OFFSET
    } else {
        offset - (paint.descent() + paint.ascent()) / 2
    }

    val y = when (align) {
        Paint.Align.LEFT -> rect.bottom - updatedOffset
        Paint.Align.CENTER -> rect.centerY()
        Paint.Align.RIGHT -> updatedOffset
    }

    val updatedX = x - paint.measureText(text) / 2
    drawFinalText(text, updatedX, y, paint)
}

private fun getPositionValue(position: Float, mainRect: RectF, tab: RectF) =
    position * (mainRect.height() - tab.height())

@OptIn(ExperimentalComposeUiApi::class)
@Preview(apiLevel = 34, showBackground = true)
@Composable
fun VerticalSliderPreview() {
    VerticalTabSlider(
        value = 0.4F,
        onValueChange = { }
    )
}
