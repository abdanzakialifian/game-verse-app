package com.gameverse.app.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gameverse.app.theme.GameVerseTheme

@Composable
fun SearchScreen() {
    SearchContent()
}

@Composable
private fun SearchContent() {

}

@Preview(showBackground = true)
@Composable
private fun SearchContentPreview() {
    GameVerseTheme {
        SearchContent()
    }
}