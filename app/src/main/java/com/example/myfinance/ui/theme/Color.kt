package com.example.myfinance.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val DarkBlue = Color.Blue

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val LightBlue = Color.Cyan

@Composable
fun getPrimaryColor(): Color {
    return if(isSystemInDarkTheme()) LightBlue else DarkBlue
}

@Composable
fun getTextColor(): Color {
    return if(isSystemInDarkTheme()) Color.White else Color.Black
}

@Composable
fun getBackgroundColor(): Color {
    return if(isSystemInDarkTheme()) Color.Black else Color.White
}