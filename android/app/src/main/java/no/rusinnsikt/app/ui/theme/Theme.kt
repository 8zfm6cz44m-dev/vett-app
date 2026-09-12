package no.rusinnsikt.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Indigo80,
    secondary = IndigoGrey80,
    tertiary = IndigoAccent80
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo40,
    secondary = IndigoGrey40,
    tertiary = IndigoAccent40
)

/**
 * Deliberately does NOT use Android 12+ dynamic (wallpaper-based) color —
 * same reasoning as the iOS app's fixed `.tint(.indigo)`: a calm, neutral
 * reference-app color identity that doesn't shift with the user's wallpaper.
 */
@Composable
fun RusinnsiktTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
