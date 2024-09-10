package org.splitties.compose.oclock.sample

import java.util.Locale

fun capitalize(text: String): String {
    return text.lowercase(Locale.getDefault()).replaceFirstChar { it.uppercase(Locale.getDefault()) }
}
