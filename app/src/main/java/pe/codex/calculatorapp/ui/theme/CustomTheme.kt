package pe.codex.calculatorapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.core.view.WindowCompat

data class ExtendedColors(
    val textColor: Color,
    val background: Color,
    val onBackground: Color,
    val backgroundVariant: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        textColor = Color.Unspecified,
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        backgroundVariant = Color.Unspecified
    )
}

@Composable
fun ExtendedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val extendedColors = when {
        darkTheme -> ExtendedColors(
            textColor = SoftWhite, background = HardBlue, onBackground = White,
            backgroundVariant = SoftBlue
        )

        else -> ExtendedColors(
            textColor = HardBlue,
            background = White,
            onBackground = HardBlue,
            backgroundVariant = SoftWhite
        )
    }
    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {

        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                window.statusBarColor = extendedColors.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
            }
        }

        MaterialTheme(
            typography = Typography,
            /* colors = ..., typography = ..., shapes = ... */
            content = {
                ProvideTextStyle(
                    value = TextStyle(color = extendedColors.textColor),
                    content
                )
            }
        )
    }

}

object ExtendedTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}