package com.gameverse.app.presentation.games.series

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.presentation.shared.GameListPaging
import com.gameverse.app.theme.GVTheme
import kotlinx.coroutines.flow.flowOf
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun GameSeriesScreen(
    gamePk: String,
    modifier: Modifier = Modifier,
    viewModel: GameSeriesViewModel = koinViewModel { parametersOf(gamePk) },
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val gamesSeriesPaging = viewModel.getGamesSeriesPaging.collectAsLazyPagingItems()

    GameSeriesContent(
        modifier = modifier,
        uiState = uiState,
        gamesSeriesPaging = gamesSeriesPaging,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun GameSeriesContent(
    uiState: GameSeriesReducer.State,
    gamesSeriesPaging: LazyPagingItems<GamesModel>,
    modifier: Modifier = Modifier,
    onIntent: (GameSeriesReducer.Intent) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        GameListPaging(
            expandedIds = uiState.expandedIds,
            gamesPaging = gamesSeriesPaging,
            onExpand = { id ->
                onIntent(GameSeriesReducer.Intent.OnExpanded(id))
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameSeriesContentPreview() {
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
        val gamesSeriesPaging = flowOf(
            PagingData.from(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = false)
                ),
                data = gamesData
            )
        ).collectAsLazyPagingItems()

        GameSeriesContent(
            uiState = GameSeriesReducer.State(
                expandedIds = gamesData.map { it.id }.toSet()
            ),
            gamesSeriesPaging = gamesSeriesPaging,
            onIntent = {}
        )
    }
}