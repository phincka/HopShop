package com.example.hopshop.ui.theme

import androidx.compose.ui.graphics.Color

val lightColors = HopShopColors(
    purple = Color(0xFF7F56D9),
    purpleLight = Color(0xFFB692F6),
    purpleWhite = Color(0xFFF9F5FF),
    purple50 = Color(0XFFd6bbfb),
    black = Color(0xFF344054),
    black90 = Color(0xFF101828),
    white = Color(0xFFffffff),
    red = Color(0xffff5c51),
    green = Color(0xFF61CA8B),
    grey = Color(0xFF667085),
    grey50 = Color(0xFFEAECF0),
    gray30 = Color(0XFFD0D5DD),
    grey20 = Color(0xFFFCFCFD),
    grey10 = Color(0XFFF2F4F7),
    grey5 = Color(0XFFf9fafb),
)

val darkColors = HopShopColors(
    purple = Color(0xFF7F56D9),
    purpleLight = Color(0xFFB692F6),
    purpleWhite = Color(0xFFF9F5FF),
    purple50 = Color(0XFFd6bbfb),
    black = Color(0xFF344054),
    black90 = Color(0xFF101828),
    white = Color(0xFFffffff),
    red = Color(0xffff5c51),
    green = Color(0xFF61CA8B),
    grey = Color(0xFF667085),
    grey50 = Color(0xFFEAECF0),
    gray30 = Color(0XFFD0D5DD),
    grey20 = Color(0xFFFCFCFD),
    grey10 = Color(0XFFF2F4F7),
    grey5 = Color(0XFFf9fafb),
)

data class HopShopColors(
    val purple: Color,
    val purpleLight: Color,
    val purpleWhite: Color,
    val purple50: Color,
    val black: Color,
    val black90: Color,
    val white: Color,
    val red: Color,
    val green: Color,
    val grey: Color,
    val grey50: Color,
    val gray30: Color,
    val grey20: Color,
    val grey10: Color,
    val grey5: Color,
)
