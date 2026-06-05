package com.gameverse.app.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gameverse.app.component.GVSearch
import com.gameverse.app.data.response.GamesResponse
import com.gameverse.app.theme.GameVerseTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(HomeReducer.Intent.OnGetGames)
    }

    HomeContent(
        uiState = uiState,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun HomeContent(
    uiState: HomeReducer.State,
    onIntent: (HomeReducer.Intent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        GVSearch(
            hint = "Search games....",
            value = uiState.searchValue,
            onValueChange = { value ->
                onIntent(HomeReducer.Intent.OnSearchValueChanged(value))
            }
        )

        GameItem(uiState.gamesData)
    }
}

@Composable
private fun GameItem(data: GamesResponse?) {
    data?.results?.forEach {
        AsyncImage(
            model = it.backgroundImage,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    GameVerseTheme {
        HomeContent(
            uiState = HomeReducer.State(),
            onIntent = {}
        )
    }
}