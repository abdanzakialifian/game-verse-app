package com.gameverse.app.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.gameverse.app.theme.GameVerseTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getGames()
    }

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