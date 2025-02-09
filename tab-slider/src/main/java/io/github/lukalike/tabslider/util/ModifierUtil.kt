package io.github.lukalike.tabslider.util

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import io.github.lukalike.tabslider.data.TabToggleState

internal suspend fun AwaitPointerEventScope.onUpdate(
    calculateCurrentPosition: (PointerInputChange) -> Float,
    onValueChangeFinished: (() -> Unit)?,
    onValueChange: (Float?, TabToggleState) -> Unit,
) {
    val down: PointerInputChange = this.awaitFirstDown()
    down.consume()

    // Invoke after the initial press
    onValueChange(calculateCurrentPosition(down), TabToggleState.Expanded)

    do {
        val event: PointerEvent = awaitPointerEvent()

        event.changes
            .forEachIndexed { _: Int, pointerInputChange: PointerInputChange ->
                // Invoke after moving
                val position = calculateCurrentPosition(pointerInputChange)
                onValueChange(position, TabToggleState.Expanded)
            }
    } while (
        event.changes.any {
            if (!it.pressed) {
                it.consume()

                // Invoke after releasing
                onValueChangeFinished?.invoke()
                onValueChange(null, TabToggleState.Collapsed)
            }

            it.pressed
        }
    )
}
