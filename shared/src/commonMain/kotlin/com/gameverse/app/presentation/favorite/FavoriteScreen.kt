package com.gameverse.app.presentation.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gameverse.app.theme.GameVerseTheme

@Composable
fun FavoriteScreen() {
    FavoriteContent()
}

@Composable
private fun FavoriteContent() {

}

@Preview(showBackground = true)
@Composable
private fun FavoriteContentPreview() {
    GameVerseTheme {
        FavoriteContent()
    }
}