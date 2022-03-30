# TabSlider
An expanding slider widget which displays selected value.

## Installation
Add the code below to your **root** `build.gradle` file:
```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```

Next, add the dependency below to your **module**'s `build.gradle` file:
```gradle
dependencies {
    implementation '???'
}
```

## Usage
You can display the slider by using **TabSlider** composable function as the following example below:
```kotlin
val tabText = remember { mutableStateOf("0") }

TabSlider(
    modifier = Modifier.padding(start = 20.dp, end = 20.dp),

    // The initial position of the tab, ranging from `0.0F` to `1.0F`
    initPosition = 0.4F,
    // The size of the tab, ranging within SMALL, MEDIUM & LARGE
    tabSize = TabSize.MEDIUM,

    // The start text of the TabSlider
    startText = "0",
    // The end text of the TabSlider
    endText = "10",
    // The text of the tab
    tabText = tabText.value,

    // The listener of the position change event, ranging from `0.0F` to `1.0F`
    // min - corresponds to minimum, max - to maximum value of the slider
    onPositionChange = {
        tabText.value = "${min + (max * it).toInt()}"
    }
)
```


## License
**Tab Slider** is released under the **Apache-2.0 License**. See [LICENSE](./LICENSE) for details.
