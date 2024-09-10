package com.louiscad.composeoclockplayground.watchface

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.LocalTextMeasurerWithoutCache
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.TapEvent
import org.splitties.compose.oclock.internal.InternalComposeOClockApi
import org.splitties.compose.oclock.sample.ForceMaxFps
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.WearPreviewSizesProvider
import org.splitties.compose.oclock.sample.capitalize
import kotlin.random.Random
import kotlin.math.sin

data class Pisca(
    val primaryColor: Color,
    val secondaryColor: Color,
    var color: Color,
    val center: Offset = Offset(0f, 0f),
) {
    fun swipeColor() {
        color = if (color == primaryColor) secondaryColor else primaryColor
    }
}

@Composable
fun SnowGlobeClock() {
    ForceMaxFps()

    Background()
    Bowl()

    Snowfall(numFlakes = 60)

    DigitalClock()

    ClickedSnowfall()

    PiscaPisca()

    Snowfall(numFlakes = 60)

    Ground()
}

@Composable
private fun PiscaPisca() {
    val isAmbient by LocalIsAmbient.current

    val colors = listOf(
        Color(0xFF_FF0000),
        Color(0xFF_00FF00),
        Color(0xFF_0000FF),
        Color(0xFF_FFFF00),
        Color(0xFF_FF00FF),
        Color(0xFF_00FFFF),
    )

    val numPiscas = 10

    val arcHeight = 80f
    val arcWidth = 480f
    val arcY = 20f

    val spacing = arcWidth / numPiscas + 1
    val radius = 6
    val initialAngle = 30f

    val piscas = remember {
        List(numPiscas) {
            val angle = initialAngle + it * (180f - 2 * initialAngle) / 9
            val x = spacing * (it + 1)
            val y = (arcY + arcHeight / 2 + (arcHeight / 2 - radius) * sin(angle * Math.PI / 180)) + 12

            Pisca(
                primaryColor = colors[it % colors.size],
                secondaryColor = colors[(it + 1) % colors.size],
                color = colors[it % colors.size],
                center = Offset(x = x, y = y.toFloat())
            )
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { _ ->
                piscas.forEach() { pisca ->
                    pisca.swipeColor()
                }
            }
            delay(1000)
        }
    }

    OClockCanvas {
        if (isAmbient) return@OClockCanvas
        if (size.width < 400) return@OClockCanvas

        drawArc(
            Color(0xFF_FFFFFF),
            startAngle = 0f,
            sweepAngle = 160f,
            useCenter = false,
            topLeft = Offset(x = 0f, y = arcY),
            size = Size(width = size.width, height = arcHeight),
            style = Stroke(width = 1.dp.toPx())
        )

        piscas.forEach() { pisca ->
            drawCircle(
                color = pisca.color,
                center = pisca.center,
                radius = radius.toFloat()
            )
        }
    }
}

@OptIn(InternalComposeOClockApi::class)
@Composable
private fun DigitalClock() {
    val currentTime = LocalTime.current

    val timeText = "${currentTime.hours.toString().padStart(2, '0')}:${currentTime.minutes.toString().padStart(2, '0')}"

    val dayOfWeek = capitalize(currentTime.dateTime.dayOfWeek.toString())
    val month = capitalize(currentTime.dateTime.month.toString())
    val dayOfMonth = currentTime.dateTime.dayOfMonth

    val dayText = "${dayOfWeek}, ${month} ${dayOfMonth}"

    val textShadow = Shadow(
        color = Color(0x33000000),
        offset = Offset(4f, 4f),
        blurRadius = 8f
    )

    val timeStyle = TextStyle(
        fontSize = 56.sp,
        fontWeight = FontWeight.W600,
        color = Color.White,
        shadow = textShadow
    )

    @OptIn(InternalComposeOClockApi::class)
    val timeMeasurer = LocalTextMeasurerWithoutCache.current

    val timeTextLayoutResult by remember(timeText) {
        derivedStateOf {
            timeMeasurer.measure(
                timeText,
                timeStyle
            )
        }
    }

    val dayStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.W400,
        color = Color.White,
        shadow = textShadow
    )

    @OptIn(InternalComposeOClockApi::class)
    val dayMeasurer = LocalTextMeasurerWithoutCache.current

    val dayTextLayoutResult by remember(dayText) {
        derivedStateOf {
            dayMeasurer.measure(
                dayText,
                dayStyle
            )
        }
    }

    val gap = - 24f
    val totalHeight = timeTextLayoutResult.size.height + dayTextLayoutResult.size.height

    OClockCanvas {
        drawText(
            brush = SolidColor(timeStyle.color),
            textLayoutResult = timeTextLayoutResult,
            topLeft = center.plus(Offset(-timeTextLayoutResult.size.width / 2f, -totalHeight / 2f))
        )
        drawText(
            brush = SolidColor(dayStyle.color),
            textLayoutResult = dayTextLayoutResult,
            topLeft = center.plus(
                Offset(
                    -dayTextLayoutResult.size.width / 2f,
                    -((totalHeight / 2f) - (timeTextLayoutResult.size.height + gap))
                )
            )
        )
    }
}

@Composable
private fun Background() {
    OClockCanvas() {
        drawCircle(Color.White)
    }
}

@Composable
private fun Bowl() {
    OClockCanvas {
        val bowlColor = Color(0xAABEEEEF)
        drawCircle(
            bowlColor
        )
    }
}

@Composable
private fun Ground() {
    OClockCanvas {
        drawArc(
            Color(0xFF_FFFFFF),
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(x = 0f, y = size.height - 200f),
            size = Size(width = size.width, height = 200f),
            style = Fill
        )
    }
}

data class Snowflake(
    var position: Offset,
    val speed: Float,
    val size: Float,


    val isPermanentSnowflake: Boolean = true,
    val color: Color = Color.White,
) {
    fun fall(totalWidth: Float, totalHeight: Float) {
        if (position.y < totalHeight) {
            position = position.copy(y = position.y + speed)

            if (isPermanentSnowflake && position.y > totalHeight) {
                position = Offset(Random.nextFloat() * totalWidth, 0f)
            }
        }
    }
}

@Composable
fun ClickedSnowfall(
    numFlakes: Int = 100,
    minSpeed: Float = 5f,
    maxSpeed: Float = 8f,
    minSize: Float = 2f,
    maxSize: Float = 9f
) {
    val snowflakes = remember { mutableListOf<Snowflake>() }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { _ ->
                snowflakes.removeIf({ it.position.y > 480 })

                snowflakes.forEach {
                    Log.d("SNOWFALL", "Snowflake position: ${it.position}")
                }
                Log.d("SNOWFALL", "Snowflakes count: ${snowflakes.size}")
            }
            delay(10000)
        }
    }

    OClockCanvas(
        onTap = { event ->
            if (event is TapEvent.Up) {
                val toFillList = numFlakes - snowflakes.size
                for (i in 0 until toFillList) {
                    snowflakes.add(
                        generateSnowflake(minSpeed, maxSpeed, minSize, maxSize, false)
                    )
                }
            }
            true
        }
    ) {
        snowflakes.forEachIndexed { index, snowflake ->
            drawCircle(snowflake.color, snowflake.size, snowflake.position)

            snowflake.fall(size.width, size.height)
        }
    }
}

@Composable
fun Snowfall(
    numFlakes: Int,
    minSpeed: Float = 0.5f,
    maxSpeed: Float = 5f,
    minSize: Float = 2f,
    maxSize: Float = 7f
) {
    val snowflakes = remember { List(numFlakes) { generateSnowflake(minSpeed, maxSpeed, minSize, maxSize) } }

    OClockCanvas {
        snowflakes.forEach { snowflake ->
            drawCircle(snowflake.color, snowflake.size, snowflake.position)

            snowflake.fall(size.width, size.height)
        }
    }
}

fun generateSnowflake(minSpeed: Float, maxSpeed: Float, minSize: Float, maxSize: Float, isPermanentSnowflake: Boolean = true): Snowflake {
    val speed = Random.nextFloat() * (maxSpeed - minSpeed) + minSpeed
    val size = Random.nextFloat() * (maxSize - minSize) + minSize
    return Snowflake(
        position = Offset(
            Random.nextFloat() * 480f,
            if (isPermanentSnowflake) Random.nextFloat() * 480f else 0f
        ),
        speed =  speed,
        size = size,
        isPermanentSnowflake = isPermanentSnowflake
    )
}



@WatchFacePreview
@Composable
private fun KotlinFanClockPreview(
    @PreviewParameter(WearPreviewSizesProvider::class) size: Dp
) = WatchFacePreview(size) {
    SnowGlobeClock()
}
