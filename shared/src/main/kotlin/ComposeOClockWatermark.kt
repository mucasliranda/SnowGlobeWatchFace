package org.splitties.compose.oclock.sample

import android.icu.text.MessageFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.AndroidFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.LocalTextMeasurerWithoutCache
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.internal.InternalComposeOClockApi
import org.splitties.compose.oclock.sample.extensions.drawTextOnPath
import org.splitties.compose.oclock.sample.extensions.rememberStateWithSize
import org.splitties.compose.oclock.sample.extensions.rotate
import org.splitties.compose.oclock.sample.extensions.text.rememberTextOnPathMeasurer
import java.util.Locale

//@Composable
//fun ComposeOClockWatermark(finalBrush: Brush) {
//    val font = remember {
//        Font(
//            googleFont = GoogleFont("Jost"),
////            googleFont = GoogleFont("Raleway"),
////            googleFont = GoogleFont("Reem Kufi"),
////            googleFont = GoogleFont("Montez"),
//            fontProvider = googleFontProvider,
//        ) as AndroidFont
//    }
//    val fontFamily = remember(font) { FontFamily(font) }
//    val context = LocalContext.current
//    val brush by produceState(finalBrush, font) {
//        val result = runCatching {
//            font.typefaceLoader.awaitLoad(context, font)
//        }
//        value = if (result.isSuccess) finalBrush else SolidColor(Color.Red)
//    }
//    val interactiveTextStyle = remember(fontFamily, brush) {
//        TextStyle(
//            brush = brush,
//            fontFamily = fontFamily,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.W600,
//            textAlign = TextAlign.Center,
//            lineHeight = 20.sp,
//            textMotion = TextMotion.Animated
//        )
//    }
//    val ambientTextStyle = rememberStateWithSize(interactiveTextStyle) {
//        interactiveTextStyle.copy(fontWeight = FontWeight.W300)
//    }
////    val textMeasurer = rememberTextOnPathMeasurer(cacheSize = 0)
//
//    val textMeasurer = rememberTextMeasurer()
//
//    val isAmbient by LocalIsAmbient.current
//    val cachedPath = remember { Path() }.apply {
//        moveTo(0f, 0f) // Mova para o ponto inicial
//        lineTo(480f, 0f) // Desenhe uma linha reta horizontal até o final do texto
//    }
//    val string = "It's Compose O'Clock!"
//    val interactiveText = rememberStateWithSize {
//        textMeasurer.measure(
//            text = string,
//            style = interactiveTextStyle
//        )
//    }
//    val ambientText = rememberStateWithSize {
//        textMeasurer.measure(
//            text = string,
//            style = ambientTextStyle.get()
//        )
//    }
//
//    val textLayoutResult by remember(string) {
//        derivedStateOf {
//            val handValueInt = type.handValue(time).toInt()
//            val timeText = timeFormatter.format(arrayOf(handValueInt))
//            measurer.measure(
//                timeText,
//                style
//            )
//        }
//    }
//
//    OClockCanvas {
//        val text = if (isAmbient) ambientText else interactiveText
//        drawText(
//            textMeasurer = textMeasurer,
//            text = "Hello, World!",
//            style = TextStyle(
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White
//            ),
////            center = Offset(size.width / 2, size.height / 2),
//            topLeft = center.plus(Offset(25f, -textLayoutResult.size.height / 2f))
//        )
//    }
//}


@OptIn(InternalComposeOClockApi::class)
@Composable
fun TryText() {
    val currentTime = LocalTime.current

    val timeText = "${currentTime.hours.toString().padStart(2, '0')}:${currentTime.minutes.toString().padStart(2, '0')}"

    val dayOfWeek = capitalize(currentTime.dateTime.dayOfWeek.toString())
    val month = capitalize(currentTime.dateTime.month.toString())
    val dayOfMonth = currentTime.dateTime.dayOfMonth

    val dayText = "${dayOfWeek}, ${month} ${dayOfMonth}"


    // TIME
    val timeStyle = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.W500,
        color = Color.White
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


    // DAY
    val dayStyle = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.W300,
        color = Color.White
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




    val gap = 32f
    val totalHeight = timeTextLayoutResult.size.height + dayTextLayoutResult.size.height + gap

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
                    -((totalHeight / 2f) - (timeTextLayoutResult.size.height))
                )
            )
        )
    }
}

@WatchFacePreview
@Composable
private fun ComposeOClockWatermarkPreview(
    @PreviewParameter(WearPreviewSizesProvider::class) size: Dp
) = WatchFacePreview(size) {
    TryText()
}
