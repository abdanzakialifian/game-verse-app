package com.gameverse.app.presentation.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import coil3.compose.AsyncImage
import com.gameverse.app.common.formatDate
import com.gameverse.app.common.formatDateTime
import com.gameverse.app.common.shimmer
import com.gameverse.app.common.toFormattedNumber
import com.gameverse.app.common.trimAfterDoubleNewline
import com.gameverse.app.component.GVRatingBadge
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.MoviesModel
import com.gameverse.app.domain.model.ScreenshotsModel
import com.gameverse.app.presentation.shared.Platforms
import com.gameverse.app.presentation.shared.GeneralError
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_favorite
import gameverse.shared.generated.resources.ic_retry
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    gameId: String,
    viewModel: DetailViewModel = koinViewModel { parametersOf(gameId) },
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val gamesScreenshotsPaging = viewModel.gamesScreenshotsPaging.collectAsLazyPagingItems()

    DetailContent(
        uiState = uiState,
        gamesScreenshotsPaging = gamesScreenshotsPaging,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun DetailContent(
    uiState: DetailReducer.State,
    gamesScreenshotsPaging: LazyPagingItems<ScreenshotsModel>,
    onIntent: (DetailReducer.Intent) -> Unit,
) {
    if (uiState.isDetailLoading) {
        DetailPlaceholder()
        return
    }

    if (uiState.detailError != null || uiState.detailData == null) {
        GeneralError {
            onIntent(DetailReducer.Intent.OnGetGameDetail(uiState.gameId))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        val isScreenshotsFirstPageLoading = gamesScreenshotsPaging.loadState.refresh is LoadState.Loading

        val isMoviesLoading = uiState.isMoviesLoading

        val isScreenshotsFirstPageError = gamesScreenshotsPaging.loadState.refresh is LoadState.Error

        val isMoviesError = uiState.moviesError != null

        DetailHeaderInformation(uiState.detailData)

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isScreenshotsFirstPageLoading || isMoviesLoading -> MediaPlaceholders()
            isScreenshotsFirstPageError && isMoviesError -> MediaErrors {
                gamesScreenshotsPaging.retry()
                onIntent(DetailReducer.Intent.OnGetGamesMovies(uiState.gameId))
            }
            else -> MediaPager(
                gamesScreenshotsPaging = gamesScreenshotsPaging,
                moviesData = uiState.moviesData
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            DetailRatingWithFavorite(
                detailData = uiState.detailData,
                isFavorite = uiState.isFavorite,
                onFavoriteClicked = { detailModel, isFavorite ->
                    onIntent(
                        DetailReducer.Intent.OnFavoriteClicked(
                            isFavorite = isFavorite,
                            detailModel = detailModel
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DetailAboutInformation(
                uiState = uiState,
                onTextLayout = { textLayoutResult ->
                    onIntent(DetailReducer.Intent.OnTextOverflow(textLayoutResult.hasVisualOverflow))
                },
                onTextExpandClicked = {
                    onIntent(DetailReducer.Intent.OnExpandDescription(!uiState.isExpandDescription))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DetailMoreInformation(uiState.detailData)
        }
    }
}

@Composable
private fun DetailPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .shimmer()
        )

        Spacer(modifier = Modifier.height(16.dp))

        MediaPlaceholders()

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(20.dp)
                            .shimmer(6.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(20.dp)
                            .shimmer(6.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(40.dp)
                        .shimmer(6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .shimmer(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                columns = GridCells.Fixed(2),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(6) {
                    Column {
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(20.dp)
                                .shimmer(6.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .shimmer(6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailHeaderInformation(detailData: DetailModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = detailData.backgroundImage,
            placeholder = ColorPainter(GVColor.outline),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            filterQuality = FilterQuality.Medium,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = GVColor.secondaryContainer.copy(alpha = 0.8f))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Platforms(detailData.parentPlatforms)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = detailData.name,
                style = GVTypography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun DetailRatingWithFavorite(
    detailData: DetailModel,
    isFavorite: Boolean,
    onFavoriteClicked: (detailModel: DetailModel, isFavorite: Boolean) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.weight(1F),
            horizontalAlignment = Alignment.Start
        ) {
            GVRatingBadge(detailData.rating)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${detailData.reviewsCount.toFormattedNumber()} Reviews",
                style = GVTypography.bodySmall,
                color = GVColor.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            modifier = Modifier
                .size(36.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onFavoriteClicked(detailData, isFavorite)
                    }
                ),
            painter = painterResource(Res.drawable.ic_favorite),
            tint = if (isFavorite) Color.Red else GVColor.onBackground,
            contentDescription = null
        )
    }

}

@Composable
private fun DetailAboutInformation(
    uiState: DetailReducer.State,
    onTextLayout: (TextLayoutResult) -> Unit,
    onTextExpandClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        Text(
            text = uiState.detailData?.description?.trimAfterDoubleNewline().orEmpty(),
            style = GVTypography.bodySmall,
            maxLines = if (uiState.isExpandDescription) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = onTextLayout
        )

        if (uiState.isTextOverflowing || uiState.isExpandDescription) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onTextExpandClicked
                ),
                text = if (uiState.isExpandDescription) "Show less" else "Show more",
                style = GVTypography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                overflow = TextOverflow.Ellipsis,
                textDecoration = TextDecoration.Underline,
            )
        }
    }
}

@Composable
private fun DetailMoreInformation(detailData: DetailModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = "Platforms",
                    style = GVTypography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = GVColor.outline
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = detailData.parentPlatforms.joinToString(", "),
                    style = GVTypography.labelSmall,
                    color = GVColor.outlineVariant
                )
            }

            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = "Genres",
                    style = GVTypography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = GVColor.outline
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = detailData.genres.joinToString(", "),
                    style = GVTypography.labelSmall,
                    color = GVColor.outlineVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = "Released Date",
                    style = GVTypography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = GVColor.outline
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = detailData.released.formatDate(),
                    style = GVTypography.labelSmall,
                    color = GVColor.outlineVariant
                )
            }

            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = "Last Modified",
                    style = GVTypography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = GVColor.outline
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = detailData.updated.formatDateTime(),
                    style = GVTypography.labelSmall,
                    color = GVColor.outlineVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = "Publishers",
                    style = GVTypography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = GVColor.outline
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = detailData.publishers.joinToString(", ") {
                        it.name
                    },
                    style = GVTypography.labelSmall,
                    color = GVColor.outlineVariant
                )
            }

            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = "Developers",
                    style = GVTypography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = GVColor.outline
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = detailData.developers.joinToString(", ") {
                        it.name
                    },
                    style = GVTypography.labelSmall,
                    color = GVColor.outlineVariant
                )
            }
        }
    }
}

@Composable
private fun MediaPager(
     gamesScreenshotsPaging: LazyPagingItems<ScreenshotsModel>,
     moviesData: List<MoviesModel>,
) {
    val hasVideo = moviesData.isNotEmpty()

    val videoUrl = moviesData.getOrNull(0)?.max.orEmpty()

    val mediaPlayerHost = remember(videoUrl) { MediaPlayerHost(mediaUrl = videoUrl) }

    val mediaPagerState = rememberPagerState {
        if (hasVideo) gamesScreenshotsPaging.itemCount + 1  else gamesScreenshotsPaging.itemCount
    }

    HorizontalPager(
        modifier = Modifier.fillMaxWidth(),
        state = mediaPagerState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        pageSpacing = 12.dp,
        beyondViewportPageCount = 1,
        pageSize = object : PageSize {
            override fun Density.calculateMainAxisPageSize(
                availableSpace: Int,
                pageSpacing: Int
            ): Int = (availableSpace * 0.80f).toInt()
        },
        key = { index ->
            if (index == 0 && hasVideo) {
                index
            } else {
                val dataIndex = if (hasVideo) index - 1 else index
                gamesScreenshotsPaging.peek(dataIndex)?.id ?: dataIndex
            }
        }
    ) { index ->
        if (index == 0 && hasVideo) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(GVShapes.small)
                    .background(GVColor.outline),
            ) {
                VideoPlayerComposable(
                    playerHost = mediaPlayerHost,
                    playerConfig = VideoPlayerConfig(
                        enablePIPControl = false,
                        isScreenLockEnabled = false,
                        isScreenResizeEnabled = false,
                        isFullScreenEnabled = false,
                    )
                )
            }
        } else {
            val dataIndex = if (hasVideo) index - 1 else index

            val result = gamesScreenshotsPaging[dataIndex]

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(GVShapes.small),
                model = result?.image,
                placeholder = ColorPainter(GVColor.outline),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                filterQuality = FilterQuality.Medium,
            )
        }
    }
}

@Composable
private fun MediaPlaceholders() {
    val pagerState = rememberPagerState { 5 }

    HorizontalPager(
        modifier = Modifier.fillMaxWidth(),
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        pageSpacing = 12.dp,
        beyondViewportPageCount = 1,
        pageSize = object : PageSize {
            override fun Density.calculateMainAxisPageSize(
                availableSpace: Int,
                pageSpacing: Int
            ): Int = (availableSpace * 0.80f).toInt()
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(GVShapes.small)
                .shimmer(cornerRadius = 12.dp),
        )
    }
}

@Composable
private fun MediaErrors(onRetry: () -> Unit) {
    val pagerState = rememberPagerState { 5 }

    HorizontalPager(
        modifier = Modifier.fillMaxWidth(),
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        pageSpacing = 12.dp,
        beyondViewportPageCount = 1,
        pageSize = object : PageSize {
            override fun Density.calculateMainAxisPageSize(
                availableSpace: Int,
                pageSpacing: Int
            ): Int = (availableSpace * 0.80f).toInt()
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(GVShapes.small)
                .background(GVColor.outline)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onRetry
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = painterResource(Res.drawable.ic_retry),
                contentDescription = null,
                tint = GVColor.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailContentPreview() {
    GVTheme {
        val gamesScreenshotsPaging = flowOf(
            PagingData.from(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.Loading
                ),
                data = listOf(
                    ScreenshotsModel(
                        id = 3976829,
                        image = "https://media.rawg.io/media/screenshots/047/047e0080a20b6987730cccd1d5ac6ea6.jpg",
                        isDeleted = false,
                        width = 1280,
                        height = 720
                    ),
                    ScreenshotsModel(
                        id = 3976830,
                        image = "https://media.rawg.io/media/screenshots/591/591262be38465590587b788fca960e22.jpg",
                        isDeleted = false,
                        width = 1280,
                        height = 720
                    ),
                    ScreenshotsModel(
                        id = 3976831,
                        image = "https://media.rawg.io/media/screenshots/6d5/6d56714c25251e6f87384e41f211f745.jpg",
                        isDeleted = false,
                        width = 1280,
                        height = 720
                    )
                )
            )
        ).collectAsLazyPagingItems()
        DetailContent(
            uiState = DetailReducer.State(
                detailData = DetailModel(
                    id = 5599,
                    developers = listOf(
                        DetailModel.DevelopersItem(
                            id = 3524,
                            name = "Rockstar North"
                        ),
                        DetailModel.DevelopersItem(
                            id = 10,
                            name = "Rockstar Games"
                        )
                    ),
                    rating = 4.5,
                    publishers = listOf(
                        DetailModel.PublishersItem(
                            id = 2155,
                            name = "Rockstar Games"
                        )
                    ),
                    parentPlatforms = (1..10).toList(),
                    ratingsCount = 9344,
                    released = "2013-09-17",
                    updated = "2026-07-03T12:09:55",
                    backgroundImage = "doctus",
                    name = "Grand Theft Auto V",
                    reviewsCount = 1654,
                    description = "Rockstar Games went bigger, since their previous installment of the series. You get the complicated and realistic world-building from Liberty City of GTA4 in the setting of lively and diverse Los Santos, from an old fan favorite GTA San Andreas. 561 different vehicles (including every transport you can operate) and the amount is rising with every update. \\nSimultaneous storytelling from three unique perspectives: \\nFollow Michael, ex-criminal living his life of leisure away from the past, Franklin, a kid that seeks the better future, and Trevor, the exact past Michael is trying to run away from. \\nGTA Online will provide a lot of additional challenge even for the experienced players, coming fresh from the story mode. Now you will have other players around that can help you just as likely as ruin your mission. Every GTA mechanic up to date can be experienced by players through the unique customizable character, and community content paired with the leveling system tends to keep everyone busy and engaged.\\n\\nEspañol\\nRockstar Games se hizo más grande desde su entrega anterior de la serie. Obtienes la construcción del mundo complicada y realista de Liberty City de GTA4 en el escenario de Los Santos, un viejo favorito de los fans, GTA San Andreas. 561 vehículos diferentes (incluidos todos los transportes que puede operar) y la cantidad aumenta con cada actualización.\\nNarración simultánea desde tres perspectivas únicas:\\nSigue a Michael, ex-criminal que vive su vida de ocio lejos del pasado, Franklin, un niño que busca un futuro mejor, y Trevor, el pasado exacto del que Michael está tratando de huir.\\nGTA Online proporcionará muchos desafíos adicionales incluso para los jugadores experimentados, recién llegados del modo historia. Ahora tendrás otros jugadores cerca que pueden ayudarte con la misma probabilidad que arruinar tu misión. Los jugadores pueden experimentar todas las mecánicas de GTA actualizadas a través del personaje personalizable único, y el contenido de la comunidad combinado con el sistema de nivelación tiende a mantener a todos ocupados y comprometidos.",
                    genres = listOf("Action", "RPG", "Shooter"),
                    )
            ),
            gamesScreenshotsPaging = gamesScreenshotsPaging,
            onIntent = {}
        )
    }
}