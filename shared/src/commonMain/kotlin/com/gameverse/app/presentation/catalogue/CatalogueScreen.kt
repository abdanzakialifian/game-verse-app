package com.gameverse.app.presentation.catalogue

import androidx.compose.foundation.background
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
import androidx.compose.material3.IconButton
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
import gameverse.shared.generated.resources.catalogue_cd_retry_loading_genres
import gameverse.shared.generated.resources.catalogue_could_not_load_more
import gameverse.shared.generated.resources.catalogue_loading_more_genres_1
import gameverse.shared.generated.resources.catalogue_loading_more_genres_2
import gameverse.shared.generated.resources.catalogue_loading_more_genres_3
import gameverse.shared.generated.resources.catalogue_loading_more_genres_4
import gameverse.shared.generated.resources.catalogue_loading_more_genres_5
import gameverse.shared.generated.resources.catalogue_popular_items
import gameverse.shared.generated.resources.ic_retry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

private val GENRE_LOADING_MESSAGES = listOf(
    Res.string.catalogue_loading_more_genres_1,
    Res.string.catalogue_loading_more_genres_2,
    Res.string.catalogue_loading_more_genres_3,
    Res.string.catalogue_loading_more_genres_4,
    Res.string.catalogue_loading_more_genres_5,
)

@Composable
fun CatalogueScreen(
    paddingValues: PaddingValues,
    viewModel: CatalogueViewModel = koinViewModel(),
    onShowGameList: (id: String) -> Unit,
) {
    val genresPaging = viewModel.getGenresPaging.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CatalogueReducer.Effect.ShowGameList -> onShowGameList(effect.id)
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
        is LoadState.Loading -> CataloguePlaceholder()

        is LoadState.Error -> GeneralError(
            modifier = Modifier.padding(paddingValues),
            onButtonClicked = {
                genresPaging.refresh()
            }
        )

        else -> LazyColumn(
            contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(genresPaging.itemCount, key = genresPaging.itemKey { it.id }) { index ->
                val result = genresPaging[index] ?: return@items

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    shape = GVShapes.medium,
                    colors = CardDefaults.cardColors(contentColor = GVColor.secondary),
                    onClick = {
                        onIntent(CatalogueReducer.Intent.SelectCategory(result.id.toString()))
                    }
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = result.imageBackground,
                            placeholder = ColorPainter(GVColor.outline),
                            error = ColorPainter(GVColor.outline),
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
                                        text = stringResource(Res.string.catalogue_popular_items),
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
                        var loadingTextRes by remember { mutableStateOf(GENRE_LOADING_MESSAGES.random()) }

                        LaunchedEffect(Unit) {
                            while (true) {
                                delay(1000L.milliseconds)
                                loadingTextRes = GENRE_LOADING_MESSAGES.random()
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(loadingTextRes),
                                style = GVTypography.labelLarge,
                            )
                        }
                    }

                    is LoadState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(onClick = { genresPaging.retry() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_retry),
                                    tint = GVColor.onPrimary,
                                    contentDescription = stringResource(Res.string.catalogue_cd_retry_loading_genres),
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = stringResource(Res.string.catalogue_could_not_load_more),
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
private fun CataloguePlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(10) {
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