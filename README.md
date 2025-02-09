[![Maven Central](https://img.shields.io/maven-central/v/io.github.lukalike/tabslider.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.lukalike/tabslider)

# TabSlider
An expanding slider widget which displays selected value.

![tab_slider](https://github.com/user-attachments/assets/5c751eda-db05-484a-b941-40200741a133)

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
    implementation 'io.github.lukalike:tabslider:2.0.0'
}
```

## Usage
You can display the slider by using **TabSlider** composable function as the following example below:
```kotlin
val horizontalRange = 0f..10f
var horizontalValue by remember { mutableFloatStateOf(.4F) }

HorizontalTabSlider(
    value = (horizontalValue * 10).toInt().toFloat(),
    onValueChange = { horizontalValue = it },
    valueRange = horizontalRange,
    sliderSettings = TabSliderDefaults.sliderSettings().copy(
        startText = floor(horizontalRange.start).toInt().toString(),
        endText = ceil(horizontalRange.endInclusive).toInt().toString()
    ),
    tabSettings = TabSliderDefaults.tabSettings()
        .copy(
            expandedSizeMultiplier = 2f,
            expansionSpeed = 100
        )
)

VerticalTabSlider(value = 0.4f, onValueChange = { })
```

## Properties

### Horizontal/VerticalTabSlider

| Parameter           | Type          | Default              | Description                                                          |
|---------------------|---------------|----------------------|----------------------------------------------------------------------|
| `value`             | Float         | -                    | Current value indication of the tab in respect to the main track     |                  
| `onValueChange`     | (Float) -> Unit | -                  | Value update callback                                                |  
| `modifier`          | Modifier      | Modifier             | Modifier of the TabSlider                                            | 
| `enabled`           | Boolean       | true                 | Controls enabled state of the TabSlider                              |         
| `valueRange`        | ClosedFloatingPointRange<Float> | 0f..1f | Range of values of the TabSlider                                 |         
| `onValueChangeFinished` |  (() -> Unit)? | null            | Lambda function to be invoked when tab value change has ended        |         
| `colors`            | TabSliderColors | TabSliderDefaults.colors() | Colors that will be used to resolve the colors used in TabSlider |         
| `sliderSettings`    | SliderSettings | TabSliderDefaults.sliderSettings() | Settings that will be used to resolve slider settings |         
| `tabSettings`       | TabSettings   | TabSliderDefaults.tabSettings() | Settings that will be used to resolve tab settings        |         
| `interactionSource` | MutableInteractionSource | remember { MutableInteractionSource() } | Interaction source allowing to listen to interaction changes in TabSlider |         

## License
**TabSlider** is released under the **Apache-2.0 License**. See [LICENSE](./LICENSE) for details.
