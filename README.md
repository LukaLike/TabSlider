[![Maven Central](https://img.shields.io/maven-central/v/io.github.lukalike/tabslider.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.lukalike%22%20AND%20a:%22tabslider%22)

# TabSlider
An expanding slider widget which displays selected value.

![tab_slider](https://user-images.githubusercontent.com/69643163/161287746-35549c56-02dc-4615-8852-c2d42512e34f.gif)

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
    implementation 'io.github.lukalike:tabslider:1.0.1'
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

## Properties

| Parameter                | Type                 | Default                  | Description                                                                                                                                                                                                                                                                                           |
| ------------------------ | -------------------- | ------------------------ | ----------------------------- |
| `modifier`               | Modifier             | Modifier                 | TabSlider's modifier          |                  
| `initPosition`           | Float                | 0F                       | Initial position of the tab, ranging from `0.0F` to `1.0F` |   
| `tabSize`                | TabSize              | TabSize.SMALL            | Size of the tab |   
| `animationDuration`      | Int                  | 300                      | Animation duration of the tab state changes in milliseconds |   
| `startText`              | String               | "0"                      | Start text of the TabSlider |   
| `endText`                | String               | "100"                    | End text of the TabSlider |   
| `tabText`                | String?              | null                     | Text of the tab |   
| `rectColor`              | Color                | Blue                     | Color of the TabSlider |   
| `collapsedTabColor`      | Color                | DarkBlue                 | Color of the collapsed tab |   
| `expandedTabColor`       | Color                | TransparentBlue          | Color of the expanded tab |   
| `rectTextColor`          | Color                | Color.White              | Text color of the TabSlider |   
| `tabTextColor`           | Color                | Color.White              | Text color of the tab |   
| `backgroundColor`        | Color                | Color.LightGray          | Color of the background |   
| `rectCornerRadius`       | CornerRadius         | CornerRadius(5F, 5F)     | Corner of the TabSlider |   
| `tabCornerRadius`        | CornerRadius         | CornerRadius(2F, 2F)     | Corner of the tab |
| `onSliderTouch`          | (() -> Unit)?        | null                     | Listener of the on slide event |   
| `onPositionChange`       | (() -> Unit)?        | null                     | Listener of the position change event, ranging from `0.0F` to `1.0F` |   
| `onSliderRelease`        | (() -> Unit)?        | null                     | Listener of the release event |   

## License
**TabSlider** is released under the **Apache-2.0 License**. See [LICENSE](./LICENSE) for details.
