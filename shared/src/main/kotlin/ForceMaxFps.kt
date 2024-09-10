package org.splitties.compose.oclock.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import org.splitties.compose.oclock.LocalTime

@Composable
fun ForceMaxFps() {
    // This composable is a invisible millis counter, just to force the max fps to be reached.

    val string = LocalTime.current.millis.toString()

    ComposeText(
        string = string, finalBrush = SolidColor(Color.Transparent)
    )
}