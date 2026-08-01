package com.gameverse.app.presentation.catalogue

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.gameverse.app.common.shimmer
import com.gameverse.app.common.toFormattedNumber
import com.gameverse.app.domain.model.GenreModel
import com.gameverse.app.presentation.shared.GeneralError
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_retry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CatalogueScreen(
    paddingValues: PaddingValues,
    viewModel: CatalogueViewModel = koinViewModel(),
    onGenresClicked: (id: String) -> Unit,
) {
    val genresPaging = viewModel.getGenresPaging.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CatalogueReducer.Effect.NavigateToGameList -> onGenresClicked(effect.id)
            }
        }
    }

    CatalogueContent(
        paddingValues = paddingValues,
        genresPaging = genresPaging,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun CatalogueContent(
    paddingValues: PaddingValues,
    genresPaging: LazyPagingItems<GenreModel>,
    onIntent: (CatalogueReducer.Intent) -> Unit,
) {
    when (genresPaging.loadState.refresh) {
        is LoadState.Loading -> GenresPlaceholder(paddingValues)
        is LoadState.Error -> GeneralError(
            modifier = Modifier.padding(paddingValues),
            onButtonClicked = {
                genresPaging.refresh()
            }
        )

        else -> LazyColumn(
            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(genresPaging.itemCount, key = genresPaging.itemKey { it.id }) { index ->
                val result = genresPaging[index] ?: return@items

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                onIntent(CatalogueReducer.Intent.SelectCategory(result.id.toString()))
                            }
                        ),
                    shape = GVShapes.medium,
                    colors = CardDefaults.cardColors(contentColor = GVColor.secondary)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = result.imageBackground,
                            placeholder = ColorPainter(GVColor.outline),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            filterQuality = FilterQuality.Medium,
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            GVColor.secondaryContainer.copy(alpha = 0.4f),
                                            GVColor.secondaryContainer.copy(alpha = 0.9f),
                                            GVColor.secondaryContainer
                                        ),
                                        startY = 200f
                                    )
                                )
                        )

                        Column(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier.weight(1F).fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = result.name,
                                    style = GVTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 16.dp
                                    )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Popular Items",
                                        style = GVTypography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                    )

                                    Text(
                                        text = result.gamesCount.toFormattedNumber(),
                                        style = GVTypography.labelLarge,
                                        color = GVColor.onSurfaceVariant
                                    )
                                }

                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    thickness = 2.dp,
                                    color = GVColor.outline
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    result.games.take(3).forEach { game ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = game.name,
                                                style = GVTypography.labelLarge
                                            )

                                            Text(
                                                text = game.added.toFormattedNumber(),
                                                style = GVTypography.labelLarge,
                                                color = GVColor.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                when (genresPaging.loadState.append) {
                    is LoadState.Loading -> {
                        val loadingMessages = listOf(
                            "Loading more genres...",
                            "Discovering new genres...",
                            "Finding your next favorite genre...",
                            "Exploring gaming categories...",
                            "Preparing more genres for you..."
                        )

                        var loadingText by remember { mutableStateOf(loadingMessages.random()) }

                        LaunchedEffect(Unit) {
                            while (true) {
                                delay(1000L.milliseconds)
                                loadingText = loadingMessages.random()
                            }
                        }

                        Box(
                            modifier = Modifier
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
                                .padding(vertical = 12.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = { genresPaging.retry() }
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

@Composable
private fun GenresPlaceholder(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(10) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = GVShapes.medium,
                colors = CardDefaults.cardColors(contentColor = GVColor.secondary)
            ) {
                Box(
                    modifier = Modifier.weight(1F).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(100.dp)
                            .height(24.dp)
                            .shimmer(12.dp),
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(16.dp)
                                .shimmer(12.dp),
                        )

                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(16.dp)
                                .shimmer(12.dp),
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        thickness = 2.dp,
                        color = GVColor.outline
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(3) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(16.dp)
                                        .shimmer(12.dp),
                                )

                                Box(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(16.dp)
                                        .shimmer(12.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogueContentPreview() {
    GVTheme {
        val genresData = List(5) {
            GenreModel(
                id = it,
                name = "Action",
                imageBackground = "https://media.rawg.io/media/games/7fa/7fa0b586293c5861ee32490e953a4996.jpg",
                gamesCount = 191772,
                games = listOf(
                    GenreModel.GamesItem(
                        id = 1,
                        name = "Grand Theft Auto V",
                        added = 22619
                    ),
                    GenreModel.GamesItem(
                        id = 2,
                        name = "The Witcher 3: Wild Hunt",
                        added = 22266
                    ),
                    GenreModel.GamesItem(
                        id = 3,
                        name = "Tomb Raider",
                        added = 17838
                    )
                )
            )
        }
        val genresPaging = flowOf(
            PagingData.from(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.Loading
                ),
                data = genresData
            )
        ).collectAsLazyPagingItems()

        CatalogueContent(
            paddingValues = PaddingValues(),
            genresPaging = genresPaging,
            onIntent = {}
        )
    }
}