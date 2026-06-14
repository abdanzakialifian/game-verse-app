package com.gameverse.app.presentation.games.list

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
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.gameverse.app.component.GVSearch
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.presentation.shared.GameListPaging
import com.gameverse.app.theme.GVTheme
import kotlinx.coroutines.flow.flowOf
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameListScreen(
    modifier: Modifier = Modifier,
    viewModel: GameListViewModel = koinViewModel(),
    onNavigateToGameSeries: (gamePk: String) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val gamesPaging = viewModel.getGamesPaging.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is GameListReducer.Effect.NavigateToGameSeries -> onNavigateToGameSeries(effect.gamePk)
            }
        }
    }

    GameListContent(
        modifier = modifier,
        uiState = uiState,
        gamesPaging = gamesPaging,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun GameListContent(
    uiState: GameListReducer.State,
    gamesPaging: LazyPagingItems<GamesModel>,
    modifier: Modifier = Modifier,
    onIntent: (GameListReducer.Intent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        GVSearch(
            hint = "Search games....",
            value = uiState.searchValue,
            onClear = {
                onIntent(GameListReducer.Intent.OnSearchValueChanged(""))
            },
            onValueChange = { value ->
                onIntent(GameListReducer.Intent.OnSearchValueChanged(value))
            }
        )

        GameListPaging(
            expandedIds = uiState.expandedIds,
            gamesPaging = gamesPaging,
            onExpand = { id ->
                onIntent(GameListReducer.Intent.OnExpanded(id))
            },
            onShowMoreClicked = { gamePk ->
                onIntent(GameListReducer.Intent.OnNavigateToGameSeries(gamePk))
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun GameListContentPreview() {
    GVTheme {
        val gamesData = List(5) {
            GamesModel(
                id = it,
                name = "Grand Theft Auto V",
                backgroundImage = "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                released = "2013-09-17",
                genres = listOf(
                    GamesModel.GenresItem(
                        id = 1,
                        name = "Action"
                    ),
                    GamesModel.GenresItem(
                        id = 2,
                        name = "RPG"
                    ),
                    GamesModel.GenresItem(
                        id = 3,
                        name = "Shooter"
                    ),
                ),
                parentPlatforms = listOf(
                    GamesModel.ParentPlatformsItem(
                        id = 1,
                        name = "Windows"
                    ),
                    GamesModel.ParentPlatformsItem(
                        id = 2,
                        name = "PlayStation"
                    ),
                    GamesModel.ParentPlatformsItem(
                        id = 3,
                        name = "Xbox"
                    ),
                    GamesModel.ParentPlatformsItem(
                        id = 4,
                        name = "Apple"
                    ),
                    GamesModel.ParentPlatformsItem(
                        id = 5,
                        name = "Apple Mac"
                    ),
                    GamesModel.ParentPlatformsItem(
                        id = 6,
                        name = "Linux"
                    ),
                    GamesModel.ParentPlatformsItem(
                        id = 7,
                        name = "Nintendo"
                    ),
                    GamesModel.ParentPlatformsItem(
                        id = 8,
                        name = "Android"
                    ),
                    GamesModel.ParentPlatformsItem(
                        id = 9,
                        name = "Others"
                    )
                ),
            )
        }
        val gamesPaging = flowOf(
            PagingData.from(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = false)
                ),
                data = gamesData
            )
        ).collectAsLazyPagingItems()

        GameListContent(
            uiState = GameListReducer.State(
                expandedIds = gamesData.map { it.id }.toSet()
            ),
            gamesPaging = gamesPaging,
            onIntent = {}
        )
    }
}