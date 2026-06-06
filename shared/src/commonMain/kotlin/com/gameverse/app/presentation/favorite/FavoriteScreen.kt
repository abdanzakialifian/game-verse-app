package com.gameverse.app.presentation.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gameverse.app.theme.GVTheme

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
    GVTheme {
        FavoriteContent()
    }
}