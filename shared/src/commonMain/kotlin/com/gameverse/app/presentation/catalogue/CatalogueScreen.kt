package com.gameverse.app.presentation.catalogue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.gameverse.app.common.toFormattedNumber
import com.gameverse.app.domain.model.GenresModel
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import kotlinx.coroutines.flow.flowOf
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CatalogueScreen(
    modifier: Modifier = Modifier,
    viewModel: CatalogueViewModel = koinViewModel()
) {
    val genresPaging = viewModel.getGenresPaging.collectAsLazyPagingItems()

    CatalogueContent(
        modifier = modifier,
        genresPaging = genresPaging
    )
}

@Composable
private fun CatalogueContent(
    genresPaging: LazyPagingItems<GenresModel>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Genres",
            style = GVTypography.headlineSmall,
            textAlign = TextAlign.Center
        )

        GenresPaging(genresPaging)
    }
}

@Composable
private fun GenresPaging(genresPaging: LazyPagingItems<GenresModel>) {
    when (genresPaging.loadState.refresh) {
        is LoadState.Loading -> {}
        is LoadState.Error -> {}
        else -> LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(genresPaging.itemCount, key = genresPaging.itemKey { it.id }) { index ->
                val result = genresPaging[index] ?: return@items

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
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
                                modifier = Modifier.weight(1F),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = result.name,
                                    style = GVTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    textAlign = TextAlign.Center,
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogueContentPreview() {
    GVTheme {
        val genresData = List(5) {
            GenresModel(
                id = it,
                name = "Action",
                imageBackground = "https://media.rawg.io/media/games/7fa/7fa0b586293c5861ee32490e953a4996.jpg",
                gamesCount = 191772,
                games = listOf(
                    GenresModel.GamesItem(
                        id = 1,
                        name = "Grand Theft Auto V",
                        added = 22619
                    ),
                    GenresModel.GamesItem(
                        id = 2,
                        name = "The Witcher 3: Wild Hunt",
                        added = 22266
                    ),
                    GenresModel.GamesItem(
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
            genresPaging = genresPaging
        )
    }
}