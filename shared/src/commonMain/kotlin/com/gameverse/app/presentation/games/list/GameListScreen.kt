package com.gameverse.app.presentation.games.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.gameverse.app.component.GVSearch
import com.gameverse.app.domain.model.GameModel
import com.gameverse.app.presentation.shared.GamePagingList
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_back
import gameverse.shared.generated.resources.ic_search
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun GameListScreen(
    genreId: String?,
    viewModel: GameListViewModel = koinViewModel { parametersOf(genreId) },
    onShowGameSeries: (gamePk: String) -> Unit,
    onShowDetail: (id: Int) -> Unit,
    onGoBack: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val gamesPaging = viewModel.getGamesPaging.collectAsLazyPagingItems()

    val navigationEventState = rememberNavigationEventState(NavigationEventInfo.None)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is GameListReducer.Effect.ShowGameSeries -> onShowGameSeries(effect.gamePk)
                is GameListReducer.Effect.ShowDetail -> onShowDetail(effect.id)
                is GameListReducer.Effect.GoBack -> onGoBack()
            }
        }
    }

    NavigationBackHandler(
        state = navigationEventState,
        onBackCompleted = {
            handleBackPressed(
                isSearchVisible = uiState.isSearchVisible,
                onIntent = viewModel::sendIntent
            )
        },
    )

    GameListContent(
        uiState = uiState,
        gamesPaging = gamesPaging,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun GameListContent(
    uiState: GameListReducer.State,
    gamesPaging: LazyPagingItems<GameModel>,
    onIntent: (GameListReducer.Intent) -> Unit,
) {
    Scaffold(
        topBar = {
            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(uiState.isSearchVisible) {
                if (uiState.isSearchVisible) {
                    focusRequester.requestFocus()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GVColor.secondaryContainer)
                    .statusBarsPadding()
                    .height(TopAppBarDefaults.TopAppBarExpandedHeight)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            handleBackPressed(
                                isSearchVisible = uiState.isSearchVisible,
                                onIntent = onIntent,
                            )
                        }
                    ),
                    painter = painterResource(Res.drawable.ic_back),
                    tint = GVColor.onPrimary,
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier
                        .weight(1F)
                        .padding(horizontal = 8.dp),
                    text = "All Games",
                    style = GVTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )

                AnimatedVisibility(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    visible = uiState.isSearchVisible,
                    enter = slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth }
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth }
                    )
                ) {
                    GVSearch(
                        modifier = Modifier.focusRequester(focusRequester),
                        hint = "Search games....",
                        value = uiState.query,
                        onClear = {
                            onIntent(GameListReducer.Intent.Search(""))
                        },
                        onValueChange = { value ->
                            onIntent(GameListReducer.Intent.Search(value))
                        }
                    )
                }

                Icon(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            onIntent(GameListReducer.Intent.ToggleSearch(true))
                        }
                    ),
                    painter = painterResource(Res.drawable.ic_search),
                    tint = GVColor.onPrimary,
                    contentDescription = null,
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            GamePagingList(
                expandedIds = uiState.expandedIds,
                gamesPaging = gamesPaging,
                onExpand = { id ->
                    onIntent(GameListReducer.Intent.Expand(id))
                },
                onShowMoreClicked = { gamePk ->
                    onIntent(GameListReducer.Intent.NavigateToGameSeries(gamePk))
                },
                onItemClicked = {
                    onIntent(GameListReducer.Intent.NavigateToDetail(it))
                }
            )
        }
    }
}

private fun handleBackPressed(
    isSearchVisible: Boolean,
    onIntent: (GameListReducer.Intent) -> Unit
) {
    if (isSearchVisible) {
        onIntent(GameListReducer.Intent.ToggleSearch(false))
    } else {
        onIntent(GameListReducer.Intent.NavigateBack)
    }
}


@Preview(showBackground = true)
@Composable
private fun GameListContentPreview() {
    GVTheme {
        val gamesData = List(5) {
            GameModel(
                id = it,
                name = "Grand Theft Auto V",
                backgroundImage = "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                released = "2013-09-17",
                genreNames = listOf("Action", "RPG", "Shooter"),
                platformIds = (1..10).toList(),
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