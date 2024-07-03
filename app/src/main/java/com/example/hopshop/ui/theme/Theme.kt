package com.example.hopshop.ui.theme

import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

@Composable
fun HopShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val hopShopColors = if (darkTheme) darkColors else lightColors

    CompositionLocalProvider(
        LocalAppColors provides hopShopColors,
    ) {
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(background = hopShopColors.white),
            typography = Typography,
            content = content
        )
    }
}

object HopShopAppTheme {
    val colors: HopShopColors
        @Composable
        get() = LocalAppColors.current
}

private val LocalAppColors = compositionLocalOf<HopShopColors> {
    error("No Colors provided")
}