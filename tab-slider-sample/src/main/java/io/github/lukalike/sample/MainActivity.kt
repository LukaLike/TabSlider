package io.github.lukalike.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.lukalike.sample.ui.theme.TabSliderTheme
import io.github.lukalike.tabslider.TabSize
import io.github.lukalike.tabslider.TabSlider

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TabSliderTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    DefaultTabSlider()
                    CustomTabSlider()
                }
            }
        }
    }

    companion object {
        const val MIN_SLIDER_VALUE = 0
        const val MAX_SLIDER_VALUE = 10
        const val TOTAL_SLIDER_VALUE = MAX_SLIDER_VALUE - MIN_SLIDER_VALUE

        const val INIT_SLIDER_VALUE = "4"

        val TransparentGray = Color(0xCC8B8B8B)
    }
}

@ExperimentalComposeUiApi
@Composable
fun DefaultTabSlider() {
    Box(modifier = Modifier.padding(top = 100.dp)) {
        TabSlider(modifier = Modifier.padding(start = 50.dp, end = 50.dp))
    }
}

@ExperimentalComposeUiApi
@Composable
fun CustomTabSlider() {
    Box(modifier = Modifier.padding(top = 100.dp)) {
        val tabText = remember { mutableStateOf(MainActivity.INIT_SLIDER_VALUE) }

        TabSlider(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            initPosition = MainActivity.INIT_SLIDER_VALUE.toFloat() / MainActivity.MAX_SLIDER_VALUE,
            tabSize = TabSize.LARGE,
            animationDuration = 800,

            startText = MainActivity.MIN_SLIDER_VALUE.toString(),
            endText = MainActivity.MAX_SLIDER_VALUE.toString(),
            tabText = tabText.value,

            rectColor = Color.Black,
            collapsedTabColor = Color.Gray,
            expandedTabColor = MainActivity.TransparentGray,
            backgroundColor = Color.Transparent,

            onPositionChange = {
                tabText.value =
                    "${MainActivity.MIN_SLIDER_VALUE + (MainActivity.TOTAL_SLIDER_VALUE * it).toInt()}"
            },
        )

    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun TabSliderPreview() {
    TabSliderTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            DefaultTabSlider()
            CustomTabSlider()
        }
    }
}
