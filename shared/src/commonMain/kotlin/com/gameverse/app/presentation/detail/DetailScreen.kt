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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
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
import androidx.paging.compose.itemKey
import chaintech.videoplayer.ui.preview.VideoPreviewComposable
import coil3.compose.AsyncImage
import com.gameverse.app.common.DetailTabs
import com.gameverse.app.common.LaunchEffectOnce
import com.gameverse.app.common.formatDate
import com.gameverse.app.common.formatDateTime
import com.gameverse.app.common.toFormattedNumber
import com.gameverse.app.common.trimAfterDoubleNewline
import com.gameverse.app.component.GVRatingBadge
import com.gameverse.app.data.response.GameDetailResponse
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.domain.model.MoviesModel
import com.gameverse.app.domain.model.ScreenshotsModel
import com.gameverse.app.presentation.shared.GamePlatforms
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_favorite
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    gameId: String,
    viewModel: DetailViewModel = koinViewModel { parametersOf(gameId) },
    onNavigateToDetailVideoPlayer: (String) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val gamesScreenshotsPaging = viewModel.gamesScreenshotsPaging.collectAsLazyPagingItems()

    LaunchEffectOnce(Unit) {
        viewModel.sendIntent(DetailReducer.Intent.OnGetGamesMovies(gameId))
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is DetailReducer.Effect.NavigateToDetailVideoPlayer -> onNavigateToDetailVideoPlayer(
                    effect.url
                )
            }
        }
    }

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        val scope = rememberCoroutineScope()

        val pagerState = rememberPagerState { DetailTabs.entries.size }

        val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

        val screenshotsPagerState = rememberPagerState { gamesScreenshotsPaging.itemCount }

        val moviesPagerState = rememberPagerState { uiState.moviesData.size }

        val isScreenshotsFirstPageLoading =
            gamesScreenshotsPaging.loadState.refresh is LoadState.Loading

        val isMoviesLoading = uiState.isMoviesLoading

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = uiState.detailData?.backgroundImage,
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
                GamePlatforms(uiState.detailData?.parentPlatforms.orEmpty())

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = uiState.detailData?.name.orEmpty(),
                    style = GVTypography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isScreenshotsFirstPageLoading || isMoviesLoading) {

        } else {
            if (gamesScreenshotsPaging.itemCount != 0 && uiState.moviesData.isNotEmpty()) {
                SecondaryTabRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(
                                selectedTabIndex = selectedTabIndex,
                                matchContentSize = false
                            ),
                            color = GVColor.onSurfaceVariant
                        )
                    }
                ) {
                    DetailTabs.entries.forEachIndexed { index, currentTab ->
                        Tab(
                            selected = selectedTabIndex == index,
                            text = {
                                Text(
                                    text = currentTab.title,
                                    style = GVTypography.labelMedium.copy(
                                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            },
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalPager(
                    modifier = Modifier.fillMaxWidth(),
                    state = pagerState,
                    userScrollEnabled = false,
                ) { index ->
                    when (index) {
                        DetailTabs.SCREENSHOTS.ordinal -> ScreenshotsPager(
                            pagerState = screenshotsPagerState,
                            gamesScreenshotsPaging = gamesScreenshotsPaging
                        )

                        DetailTabs.TRAILERS.ordinal -> TrailersPager(
                            pagerState = moviesPagerState,
                            moviesData = uiState.moviesData,
                            onTrailerClicked = { url ->
                                onIntent(DetailReducer.Intent.OnNavigateToDetailVideoPlayer(url))
                            }
                        )
                    }
                }
            }

            if (gamesScreenshotsPaging.itemCount != 0 && uiState.moviesData.isEmpty()) {
                ScreenshotsPager(
                    pagerState = screenshotsPagerState,
                    gamesScreenshotsPaging = gamesScreenshotsPaging
                )
            }

            if (gamesScreenshotsPaging.itemCount == 0 && uiState.moviesData.isNotEmpty()) {
                TrailersPager(
                    pagerState = moviesPagerState,
                    moviesData = uiState.moviesData,
                    onTrailerClicked = { url ->
                        onIntent(DetailReducer.Intent.OnNavigateToDetailVideoPlayer(url))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.weight(1F),
                    horizontalAlignment = Alignment.Start
                ) {
                    GVRatingBadge(uiState.detailData?.rating ?: 0.0)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${uiState.detailData?.reviewsCount?.toFormattedNumber()} Reviews",
                        style = GVTypography.bodySmall,
                        color = GVColor.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(Res.drawable.ic_favorite),
                    tint = GVColor.onBackground,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                    onTextLayout = { textLayoutResult ->
                        onIntent(DetailReducer.Intent.OnTextOverflow(textLayoutResult.hasVisualOverflow))
                    }
                )

                if (uiState.isTextOverflowing || uiState.isExpandDescription) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                onIntent(DetailReducer.Intent.OnExpandDescription(!uiState.isExpandDescription))
                            }
                        ),
                        text = if (uiState.isExpandDescription) "Show less" else "Show more",
                        style = GVTypography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                            text = uiState.detailData?.parentPlatforms?.joinToString(", ") {
                                it.name
                            }.orEmpty(),
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
                            text = uiState.detailData?.genres?.joinToString(", ") {
                                it.name
                            }.orEmpty(),
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
                            text = uiState.detailData?.released.formatDate(),
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
                            text = uiState.detailData?.updated.formatDateTime(),
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
                            text = uiState.detailData?.publishers?.joinToString(", ") {
                                it.name
                            }.orEmpty(),
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
                            text =  uiState.detailData?.developers?.joinToString(", ") {
                                it.name
                            }.orEmpty(),
                            style = GVTypography.labelSmall,
                            color = GVColor.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenshotsPager(
    pagerState: PagerState,
    gamesScreenshotsPaging: LazyPagingItems<ScreenshotsModel>,
) {
    HorizontalPager(
        modifier = Modifier.fillMaxWidth(),
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        pageSpacing = 12.dp,
        pageSize = object : PageSize {
            override fun Density.calculateMainAxisPageSize(
                availableSpace: Int,
                pageSpacing: Int
            ): Int = (availableSpace * 0.80f).toInt()
        },
        key = gamesScreenshotsPaging.itemKey { it.id }
    ) { index ->
        val result = gamesScreenshotsPaging[index] ?: return@HorizontalPager

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(GVShapes.small),
            model = result.image,
            placeholder = ColorPainter(GVColor.outline),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            filterQuality = FilterQuality.Medium,
        )
    }
}

@Composable
private fun TrailersPager(
    pagerState: PagerState,
    moviesData: List<MoviesModel>,
    onTrailerClicked: (String) -> Unit,
) {
    HorizontalPager(
        modifier = Modifier.fillMaxWidth(),
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        pageSpacing = 12.dp,
        pageSize = object : PageSize {
            override fun Density.calculateMainAxisPageSize(
                availableSpace: Int,
                pageSpacing: Int
            ): Int = (availableSpace * 0.80f).toInt()
        },
        key = { moviesData.getOrNull(it)?.id ?: 0 }
    ) { index ->
        val result = moviesData.getOrNull(index) ?: return@HorizontalPager

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(
                    color = GVColor.outline,
                    shape = GVShapes.small
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onTrailerClicked(result.max)
                    }
                ),
        ) {
            VideoPreviewComposable(
                url = result.jsonMember480,
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
                    ratingsCount = 9344,
                    released = "2013-09-17",
                    updated = "2026-07-03T12:09:55",
                    backgroundImage = "doctus",
                    name = "Grand Theft Auto V",
                    reviewsCount = 1654,
                    description = "Rockstar Games went bigger, since their previous installment of the series. You get the complicated and realistic world-building from Liberty City of GTA4 in the setting of lively and diverse Los Santos, from an old fan favorite GTA San Andreas. 561 different vehicles (including every transport you can operate) and the amount is rising with every update. \\nSimultaneous storytelling from three unique perspectives: \\nFollow Michael, ex-criminal living his life of leisure away from the past, Franklin, a kid that seeks the better future, and Trevor, the exact past Michael is trying to run away from. \\nGTA Online will provide a lot of additional challenge even for the experienced players, coming fresh from the story mode. Now you will have other players around that can help you just as likely as ruin your mission. Every GTA mechanic up to date can be experienced by players through the unique customizable character, and community content paired with the leveling system tends to keep everyone busy and engaged.\\n\\nEspañol\\nRockstar Games se hizo más grande desde su entrega anterior de la serie. Obtienes la construcción del mundo complicada y realista de Liberty City de GTA4 en el escenario de Los Santos, un viejo favorito de los fans, GTA San Andreas. 561 vehículos diferentes (incluidos todos los transportes que puede operar) y la cantidad aumenta con cada actualización.\\nNarración simultánea desde tres perspectivas únicas:\\nSigue a Michael, ex-criminal que vive su vida de ocio lejos del pasado, Franklin, un niño que busca un futuro mejor, y Trevor, el pasado exacto del que Michael está tratando de huir.\\nGTA Online proporcionará muchos desafíos adicionales incluso para los jugadores experimentados, recién llegados del modo historia. Ahora tendrás otros jugadores cerca que pueden ayudarte con la misma probabilidad que arruinar tu misión. Los jugadores pueden experimentar todas las mecánicas de GTA actualizadas a través del personaje personalizable único, y el contenido de la comunidad combinado con el sistema de nivelación tiende a mantener a todos ocupados y comprometidos.",
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

                    )
            ),
            gamesScreenshotsPaging = gamesScreenshotsPaging,
            onIntent = {}
        )
    }
}