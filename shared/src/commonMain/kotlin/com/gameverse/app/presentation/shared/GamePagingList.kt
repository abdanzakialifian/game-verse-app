package com.gameverse.app.presentation.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_retry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GamePagingList(
    expandedIds: Set<Int>,
    gamesPaging: LazyPagingItems<GamesModel>,
    modifier: Modifier = Modifier,
    onShowMoreClicked: ((gamePk: String) -> Unit)? = null,
    onExpand: (id: Int) -> Unit,
    onItemClicked: (id: Int) -> Unit
) {
    when (gamesPaging.loadState.refresh) {
        is LoadState.Loading -> LoadingPlaceholders()
        is LoadState.Error -> GeneralError(
            onButtonClicked = {
                gamesPaging.retry()
            }
        )
        else -> {
            if (gamesPaging.itemCount == 0) {
                GeneralEmpty()
                return
            }

            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(gamesPaging.itemCount, key = gamesPaging.itemKey { it.id }) { index ->
                    val result = gamesPaging[index] ?: return@items

                    GameItem(
                        game = result,
                        expandedIds = expandedIds,
                        onShowMoreClicked = onShowMoreClicked,
                        onExpand = onExpand,
                        onItemClicked = onItemClicked
                    )
                }

                item {
                    when (gamesPaging.loadState.append) {
                        is LoadState.Loading -> {
                            val loadingMessages = listOf(
                                "Loading more games...",
                                "Finding more games...",
                                "Looking for hidden gems...",
                                "Exploring new worlds...",
                                "Preparing the next adventure..."
                            )

                            var loadingText by remember { mutableStateOf(loadingMessages.random()) }

                            LaunchedEffect(Unit) {
                                while (true) {
                                    delay(1000L.milliseconds)
                                    loadingText = loadingMessages.random()
                                }
                            }

                            Box(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = loadingText,
                                    style = GVTypography.labelLarge,
                                )
                            }
                        }

                        is LoadState.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = { gamesPaging.retry() }
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_retry),
                                    tint = GVColor.onPrimary,
                                    contentDescription = null,
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Couldn't load more games",
                                    style = GVTypography.labelLarge,
                                )
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GamePagingListPreview() {
    GVTheme {
        val gamesData = List(5) {
            GamesModel(
                id = it,
                name = "Grand Theft Auto V",
                backgroundImage = "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                released = "2013-09-17",
                genres = listOf("Action", "RPG", "Shooter"),
                parentPlatforms = (1..10).toList(),
            )
        }
        val gamesPaging = flowOf(
            PagingData.from(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true)
                ),
                data = gamesData
            )
        ).collectAsLazyPagingItems()

        GamePagingList(
            expandedIds = gamesData.map { it.id }.toSet(),
            gamesPaging = gamesPaging,
            onExpand = {},
            onShowMoreClicked = {},
            onItemClicked = {}
        )
    }
}

@Preview
@Composable
private fun GamePagingListInitialLoadPreview() {
    GVTheme {
        val gamesPaging = flowOf(
            PagingData.from(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Loading,
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true)
                ),
                data = emptyList<GamesModel>()
            )
        ).collectAsLazyPagingItems()

        GamePagingList(
            expandedIds = emptySet(),
            gamesPaging = gamesPaging,
            onExpand = {},
            onShowMoreClicked = {},
            onItemClicked = {}
        )
    }
}

@Preview
@Composable
private fun GamePagingListLoadMorePreview() {
    GVTheme {
        val gamesData = List(2) {
            GamesModel(
                id = it,
                name = "Grand Theft Auto V",
                backgroundImage = "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                released = "2013-09-17",
                genres = listOf("Action", "RPG", "Shooter"),
                parentPlatforms = (1..10).toList(),
            )
        }
        val gamesPaging = flowOf(
            PagingData.from(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.Loading
                ),
                data = gamesData
            )
        ).collectAsLazyPagingItems()

        GamePagingList(
            expandedIds = emptySet(),
            gamesPaging = gamesPaging,
            onExpand = {},
            onShowMoreClicked = {},
            onItemClicked = {}
        )
    }
}

@Preview
@Composable
private fun GamePagingListErrorPreview() {
    GVTheme {
        val gamesData = List(2) {
            GamesModel(
                id = it,
                name = "Grand Theft Auto V",
                backgroundImage = "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                released = "2013-09-17",
                genres = listOf("Action", "RPG", "Shooter"),
                parentPlatforms = (1..10).toList(),
            )
        }
        val gamesPaging = flowOf(
            PagingData.from(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.Error(Throwable())
                ),
                data = gamesData
            )
        ).collectAsLazyPagingItems()

        GamePagingList(
            expandedIds = emptySet(),
            gamesPaging = gamesPaging,
            onExpand = {},
            onShowMoreClicked = {},
            onItemClicked = {}
        )
    }
}