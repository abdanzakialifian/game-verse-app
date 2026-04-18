package com.gameverse.app

import androidx.compose.runtime.Composable
import com.gameverse.app.presentation.main.MainApp
import com.gameverse.app.theme.GameVerseTheme

@Composable
fun App() {
    GameVerseTheme {
        MainApp()
    }
}