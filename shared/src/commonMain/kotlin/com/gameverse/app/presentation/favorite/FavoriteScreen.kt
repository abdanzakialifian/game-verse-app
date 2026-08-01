package com.gameverse.app.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gameverse.app.presentation.shared.GameListItem
import com.gameverse.app.presentation.shared.GeneralEmpty
import com.gameverse.app.presentation.shared.GeneralError
import com.gameverse.app.theme.GVTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteScreen(
    paddingValues: PaddingValues,
    viewModel: FavoriteViewModel = koinViewModel(),
    onNavigateToGameSeries: (gamePk: String) -> Unit,
    onNavigateToDetail: (gamePk: String) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect {
            when (it) {
                is FavoriteReducer.Effect.NavigateToGameSeries -> onNavigateToGameSeries(it.gamePk)
                is FavoriteReducer.Effect.NavigateToDetail -> onNavigateToDetail(it.gamePk)
            }
        }
    }

    FavoriteContent(
        paddingValues = paddingValues,
        uiState = uiState,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun FavoriteContent(
    paddingValues: PaddingValues,
    uiState: FavoriteReducer.State,
    onIntent: (FavoriteReducer.Intent) -> Unit,
) {
    if (uiState.error != null) {
        GeneralError(
            modifier = Modifier.padding(paddingValues),
            onButtonClicked = {
                onIntent(FavoriteReducer.Intent.OnGetFavorites)
            }
        )
        return
    }

    if (uiState.gameList.isEmpty()) {
        GeneralEmpty()
        return
    }

    LazyColumn(
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(uiState.gameList, key = { it.id }) { result ->
            GameListItem(
                game = result,
                expandedIds = uiState.expandedIds,
                onShowMoreClicked = {
                    onIntent(FavoriteReducer.Intent.OnNavigateToGameSeries(it))
                },
                onExpand = {
                    onIntent(FavoriteReducer.Intent.OnExpanded(it))
                },
                onItemClicked = {
                    onIntent(FavoriteReducer.Intent.OnNavigateToDetail(it.toString()))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteContentPreview() {
    GVTheme {
        FavoriteContent(
            paddingValues = PaddingValues(),
            uiState = FavoriteReducer.State(),
            onIntent = {}
        )
    }
}