package com.gameverse.app.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gameverse.app.theme.GameVerseTheme

@Composable
fun HomeScreen() {
    HomeContent()
}

@Composable
private fun HomeContent() {

}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    GameVerseTheme {
        HomeContent()
    }
}