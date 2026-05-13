package com.suraj.weathersnap.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val DarkBackground    = Color(0xFF1A1F0F)
val CardBackground    = Color(0xFF252B18)
val CardSurface       = Color(0xFF2C3320)
val AccentGreen       = Color(0xFF8FA840)
val TextPrimary       = Color(0xFFE8EDD4)
val TextSecondary     = Color(0xFF9BA882)
val TextMuted         = Color(0xFF6B7A55)
val TempOrange        = Color(0xFFD4A847)
val HumidityBlue      = Color(0xFF5A8FC0)
val WindTeal          = Color(0xFF4AACAA)
val PressureOrange    = Color(0xFFD4763A)
val OriginalOrange    = Color(0xFFCC7733)
val CompressedTeal    = Color(0xFF4AACAA)
val DividerColor      = Color(0xFF3A4228)
val InputBorder       = Color(0xFF4A5535)
val ChipBackground    = Color(0xFF303D1E)
val HeaderGreenLight  = Color(0xFFD4E88A)
val HeaderGreenDark   = Color(0xFF9AB840)
val DarkButtonBg      = Color(0xFF2A3515)

private val DarkColors = darkColorScheme(
    background = DarkBackground,
    surface    = CardBackground,
    primary    = AccentGreen,
    onBackground = TextPrimary,
    onSurface  = TextPrimary,
)

@Composable
fun WeatherSnapTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColors, content = content)
}
