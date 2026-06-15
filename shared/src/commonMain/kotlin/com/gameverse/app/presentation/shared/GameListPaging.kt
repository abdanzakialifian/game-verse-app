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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_retry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GameListPaging(
    expandedIds: Set<Int>,
    gamesPaging: LazyPagingItems<GamesModel>,
    modifier: Modifier = Modifier,
    onExpand: (id: Int) -> Unit,
    onShowMoreClicked: ((gamePk: String) -> Unit)? = null,
) {
    when (gamesPaging.loadState.refresh) {
        is LoadState.Loading -> GamePlaceholders()
        is LoadState.Error -> GameError(
            onButtonClicked = {
                gamesPaging.retry()
            }
        )
        else -> {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(gamesPaging.itemCount, key = gamesPaging.itemKey { it.id }) { index ->
                    val result = gamesPaging[index] ?: return@items

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = GVShapes.medium,
                        colors = CardDefaults.cardColors(contentColor = GVColor.secondary)
                    ) {
                        Column {
                            AsyncImage(
                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                model = result.backgroundImage,
                                placeholder = ColorPainter(GVColor.outline),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                filterQuality = FilterQuality.Medium,
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            GamePlatforms(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                platforms = result.parentPlatforms
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = result.name,
                                style = GVTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            )

                            GameInformation(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                released = result.released,
                                genres = result.genres,
                                isExpanded = result.id in expandedIds,
                                onButtonClicked = onShowMoreClicked?.let { callback ->
                                    {
                                        callback(result.id.toString())
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = {
                                            onExpand(result.id)
                                        }
                                    ),
                                text = if (result.id in expandedIds) "View less" else "View more",
                                style = GVTypography.labelMedium.copy(textDecoration = TextDecoration.Underline),
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                item {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
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

                                Text(
                                    text = loadingText,
                                    style = GVTypography.labelLarge,
                                )
                            }

                            is LoadState.Error -> {
                                Column(
                                    modifier = Modifier.clickable(
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
}

@Preview
@Composable
private fun GameListPagingPreview() {
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
                    append = LoadState.Loading
                ),
                data = gamesData
            )
        ).collectAsLazyPagingItems()

        GameListPaging(
            expandedIds = gamesData.map { it.id }.toSet(),
            gamesPaging = gamesPaging,
            onExpand = {},
            onShowMoreClicked = {}
        )
    }
}