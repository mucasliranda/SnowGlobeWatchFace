package com.louiscad.composeoclockplayground

import androidx.compose.runtime.Composable
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import kotlinx.coroutines.flow.*
import org.splitties.compose.oclock.ComposeWatchFaceService
import com.louiscad.composeoclockplayground.watchface.SnowGlobeClock

class SampleWatchFaceService : ComposeWatchFaceService(
    complicationSlotIds = emptySet(),
    invalidationMode = InvalidationMode.WaitForInvalidation,
    watchFaceType = WatchFaceType.DIGITAL
) {

    @Composable
    override fun Content(complicationData: Map<Int, StateFlow<ComplicationData>>) {
        SnowGlobeClock()
    }

    override fun supportedComplicationTypes(slotId: Int) = listOf(
        ComplicationType.RANGED_VALUE,
        ComplicationType.SHORT_TEXT
    )
}
