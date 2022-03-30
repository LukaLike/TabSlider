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
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

/**
 * Tab slider
 *
 * @param modifier TabSlider's modifier
 * @param initPosition the initial position of the tab, ranging from `0.0F` to `1.0F`
 * @param tabSize the size of the tab
 * @param animationDuration the animation duration of the tab state changes in milliseconds
 * @param startText the start text of the TabSlider
 * @param endText the end text of the TabSlider
 * @param tabText the text of the tab
 * @param rectColor the color of the TabSlider
 * @param collapsedTabColor the color of the collapsed tab
 * @param expandedTabColor the color of the expanded tab
 * @param rectTextColor the text color of the TabSlider
 * @param tabTextColor the text color of the tab
 * @param backgroundColor the color of the background
 * @param rectCornerRadius the corner of the TabSlider
 * @param tabCornerRadius the corner of the tab
 * @param onSliderTouch the listener of the on slide event
 * @param onPositionChange the listener of the position change event, ranging from `0.0F` to `1.0F`
 * @param onSliderRelease the listener of the release event
 */
@ExperimentalComposeUiApi
@Composable
fun TabSlider(
    modifier: Modifier = Modifier,

    initPosition: Float = 0F,
    tabSize: TabSize = TabSize.SMALL,
    animationDuration: Int = 300,

    startText: String = "0",
    endText: String = "100",
    tabText: String? = null,

    rectColor: Color = Blue,
    collapsedTabColor: Color = DarkBlue,
    expandedTabColor: Color = TransparentBlue,
    rectTextColor: Color = Color.White,
    tabTextColor: Color = Color.White,
    backgroundColor: Color = Color.LightGray,

    rectCornerRadius: CornerRadius = CornerRadius(RECT_CORNER_RADIUS, RECT_CORNER_RADIUS),
    tabCornerRadius: CornerRadius = CornerRadius(TAB_CORNER_RADIUS, TAB_CORNER_RADIUS),

    onSliderTouch: (() -> Unit)? = null,
    onPositionChange: ((Float) -> Unit)? = null,
    onSliderRelease: (() -> Unit)? = null
) {
    val density = LocalContext.current.resources.displayMetrics.density

    // Main rectangle, containing start, end texts & movable rectangle AKA tab
    val mainRect = RectF()

    // Movable rectangle AKA tab
    val tab = RectF()

    // Current position of the tab
    var position by remember { mutableStateOf(max(0F, min(1F, initPosition))) }

    // State & transition data of the tab
    var state by remember { mutableStateOf(TabState.Collapsed) }
    val transitionData = updateTransitionData(
        state, collapsedTabColor, tabSize.value * density, expandedTabColor, animationDuration
    )

    Column(
        modifier = modifier
            .height(tabSize.value.dp * EXPANDED_HEIGHT)
            .background(backgroundColor)
            .padding(top = (tabSize.value.dp * EXPANDED_HEIGHT) - tabSize.value.dp)

    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(tabSize.value.dp)
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            onUpdate(
                                onSliderTouch,
                                onPositionChange,
                                onSliderRelease,
                                mainRect,
                                tab
                            ) { newPos, newTabState ->
                                newPos?.let { position = it }
                                state = newTabState
                            }
                        }
                    }
                }
                .onSizeChanged {
                    onSizeChanged(it, mainRect, tab)
                },
            onDraw = {
                // Draw main rectangle
                drawRoundRect(color = rectColor, cornerRadius = rectCornerRadius)

                // Draw start & end texts
                drawIntoCanvas {
                    it.drawText(
                        startText, mainRect, rectTextColor, Paint.Align.LEFT,
                        tabSize.value * density / 2.0F, density, mainRect.centerY()
                    )
                }
                drawIntoCanvas {
                    it.drawText(
                        endText, mainRect, rectTextColor, Paint.Align.RIGHT,
                        tabSize.value * density / 2.0F, density, mainRect.centerY()
                    )
                }

                // Draw tab
                drawRoundRect(
                    color = transitionData.color,
                    topLeft = Offset(
                        getPositionValue(position, mainRect, tab),
                        tabSize.value * density
                    ),
                    cornerRadius = tabCornerRadius,
                    size = Size(transitionData.size.width, -transitionData.size.height)
                )

                // Update tab position
                tab.offsetTo(getPositionValue(position, mainRect, tab), transitionData.offset.y)

                // Draw label
                drawIntoCanvas {
                    val text = tabText ?: (position * 100).toInt().toString()

                    // Calculate appropriate position for the text of the tab
                    val expandedSize = mainRect.height() * EXPANDED_HEIGHT - mainRect.height()
                    val temp = expandedSize / 2 + (mainRect.height() / 2 - expandedSize)

                    val pctFromExpanded = (transitionData.size.height - mainRect.height()) /
                            (mainRect.height() * EXPANDED_HEIGHT - mainRect.height())
                    val currentPct = temp * pctFromExpanded

                    val y =
                        (mainRect.height() / 2 - (transitionData.size.height + currentPct - mainRect.height()))

                    if (state == TabState.Collapsed) {
                        it.drawText(
                            text, tab, tabTextColor, Paint.Align.CENTER,
                            LABEL_TEXT_OFFSET, density, y
                        )
                    } else if (state == TabState.Expanded) {
                        it.drawText(
                            text, tab, tabTextColor, Paint.Align.CENTER,
                            LABEL_TEXT_OFFSET, density, y
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun updateTransitionData(
    tabState: TabState,
    collapsedTabColor: Color,
    collapsedTabHeight: Float,
    expandedTabColor: Color,
    animationDuration: Int
): TransitionData {
    val transition = updateTransition(tabState, label = "Transition")

    val size = transition.animateSize(
        transitionSpec = { tween(durationMillis = animationDuration) },
        label = "Offset"
    ) { state ->
        when (state) {
            TabState.Collapsed -> Size(collapsedTabHeight, collapsedTabHeight)
            TabState.Expanded -> Size(collapsedTabHeight, collapsedTabHeight * EXPANDED_HEIGHT)
        }
    }
    val offset = transition.animateOffset(
        transitionSpec = { tween(durationMillis = animationDuration) },
        label = "Offset"
    ) { state ->
        when (state) {
            TabState.Collapsed -> Offset(0F, 0F)
            TabState.Expanded -> Offset(
                0F,
                -(collapsedTabHeight * EXPANDED_HEIGHT - collapsedTabHeight)
            )
        }
    }
    val color = transition.animateColor(
        transitionSpec = { tween(durationMillis = animationDuration) },
        label = "Color"
    ) { state ->
        when (state) {
            TabState.Collapsed -> collapsedTabColor
            TabState.Expanded -> expandedTabColor
        }
    }

    return remember(transition) { TransitionData(size, offset, color) }
}

private suspend fun AwaitPointerEventScope.onUpdate(
    onSliderTouch: (() -> Unit)?,
    onPositionChange: ((Float) -> Unit)?,
    onSliderRelease: (() -> Unit)?,
    mainRect: RectF,
    tab: RectF,
    param: (Float?, TabState) -> Unit
) {
    val down: PointerInputChange = this.awaitFirstDown()
    down.consumeDownChange()

    // Invoke after the initial press
    onSliderTouch?.invoke()
    param.invoke(calculateCurrentPosition(down, mainRect, tab), TabState.Expanded)

    do {
        val event: PointerEvent = awaitPointerEvent()

        event.changes
            .forEachIndexed { _: Int, pointerInputChange: PointerInputChange ->
                // Invoke after moving
                val position = calculateCurrentPosition(pointerInputChange, mainRect, tab)

                onPositionChange?.invoke(position)
                param.invoke(position, TabState.Expanded)
            }
    } while (
        event.changes.any {
            if (!it.pressed) {
                it.consumeDownChange()

                // Invoke after releasing
                onSliderRelease?.invoke()
                param.invoke(null, TabState.Collapsed)
            }

            it.pressed
        }
    )
}

private fun onSizeChanged(intSize: IntSize, mainRect: RectF, tab: RectF) {
    mainRect.set(0F, 0F, intSize.width.toFloat(), intSize.height.toFloat())
    tab.set(0F, 0F, intSize.height.toFloat(), intSize.height.toFloat())
}

private fun calculateCurrentPosition(inputChange: PointerInputChange, mainRect: RectF, tab: RectF) =
    max(
        0F,
        min(1F, (inputChange.position.x - (tab.width() / 2)) / (mainRect.width() - tab.width()))
    )

private fun getPositionValue(position: Float, mainRect: RectF, tab: RectF) =
    position * (mainRect.width() - tab.width())

private fun Canvas.drawText(
    text: String, rect: RectF,
    color: Color, align: Paint.Align,
    offset: Float, density: Float, y: Float
) {
    val paint = Paint()

    paint.color = color.toArgb()
    paint.textAlign = align
    paint.textSize = TEXT_SIZE * density

    val updatedOffset = if (paint.measureText(text) > rect.height()) {
        TEXT_OFFSET
    } else {
        offset - paint.measureText(text) / 2
    }

    val x = when (align) {
        Paint.Align.LEFT -> updatedOffset
        Paint.Align.CENTER -> rect.centerX()
        Paint.Align.RIGHT -> rect.right - updatedOffset
    }

    val updatedY = y - (paint.descent() + paint.ascent()) / 2
    nativeCanvas.drawText(text, 0, text.length, x, updatedY, paint)
}
