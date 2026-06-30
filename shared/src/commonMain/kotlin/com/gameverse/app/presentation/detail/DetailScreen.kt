package com.gameverse.app.presentation.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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
import com.gameverse.app.common.trimAfterDoubleNewline
import com.gameverse.app.data.response.AddedByStatus
import com.gameverse.app.data.response.EsrbRating
import com.gameverse.app.data.response.GameDetailResponse
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.ScreenshotsModel
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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
            when(effect) {
                is DetailReducer.Effect.NavigateToDetailVideoPlayer -> onNavigateToDetailVideoPlayer(effect.url)
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
    ) {
        val scope = rememberCoroutineScope()

        val pagerState = rememberPagerState { DetailTabs.entries.size }

        val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

        val screenshotsPagerState = rememberPagerState { gamesScreenshotsPaging.itemCount }

        val moviesPagerState = rememberPagerState { uiState.moviesData.size }

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
            model = uiState.detailData?.backgroundImage,
            placeholder = ColorPainter(GVColor.outline),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            filterQuality = FilterQuality.Medium,
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                DetailTabs.SCREENSHOTS.ordinal -> HorizontalPager(
                    modifier = Modifier.fillMaxWidth(),
                    state = screenshotsPagerState,
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

                DetailTabs.TRAILERS.ordinal -> HorizontalPager(
                    modifier = Modifier.fillMaxWidth(),
                    state = moviesPagerState,
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    pageSpacing = 12.dp,
                    pageSize = object : PageSize {
                        override fun Density.calculateMainAxisPageSize(
                            availableSpace: Int,
                            pageSpacing: Int
                        ): Int = (availableSpace * 0.80f).toInt()
                    },
                    key = { uiState.moviesData.getOrNull(it)?.id ?: 0 }
                ) { index ->
                    val result = uiState.moviesData.getOrNull(index) ?: return@HorizontalPager

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(GVShapes.small)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    onIntent(DetailReducer.Intent.OnNavigateToDetailVideoPlayer(result.max))
                                }
                            ),
                    ) {
                        VideoPreviewComposable(result.max)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = uiState.detailData?.name.orEmpty(),
                style = GVTypography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = uiState.detailData?.publishers
                    ?.filter { it.name != null }
                    ?.joinToString(", ") {
                        it.name.toString()
                    }.orEmpty(),
                style = GVTypography.bodySmall,
                color = GVColor.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = uiState.detailData?.descriptionRaw?.trimAfterDoubleNewline().orEmpty(),
                style = GVTypography.bodySmall,
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
                    added = 6205,
                    developers = listOf(),
                    nameOriginal = "Gabriel Bond",
                    rating = 4.5,
                    gameSeriesCount = 1539,
                    playtime = 6282,
                    platforms = listOf(),
                    ratingTop = 1308,
                    reviewsTextCount = 7616,
                    publishers = listOf(
                        GameDetailResponse.PublishersItem(
                            name = "Rockstar Games"
                        )
                    ),
                    achievementsCount = 4742,
                    id = 5599,
                    parentPlatforms = listOf(),
                    redditName = "Galen Wallace",
                    ratingsCount = 9344,
                    slug = "dolore",
                    released = "elementum",
                    youtubeCount = 2103,
                    moviesCount = 1526,
                    descriptionRaw = "Rockstar Games went bigger, since their previous installment of the series. You get the complicated and realistic world-building from Liberty City of GTA4 in the setting of lively and diverse Los Santos, from an old fan favorite GTA San Andreas. 561 different vehicles (including every transport you can operate) and the amount is rising with every update. \\nSimultaneous storytelling from three unique perspectives: \\nFollow Michael, ex-criminal living his life of leisure away from the past, Franklin, a kid that seeks the better future, and Trevor, the exact past Michael is trying to run away from. \\nGTA Online will provide a lot of additional challenge even for the experienced players, coming fresh from the story mode. Now you will have other players around that can help you just as likely as ruin your mission. Every GTA mechanic up to date can be experienced by players through the unique customizable character, and community content paired with the leveling system tends to keep everyone busy and engaged.\\n\\nEspañol\\nRockstar Games se hizo más grande desde su entrega anterior de la serie. Obtienes la construcción del mundo complicada y realista de Liberty City de GTA4 en el escenario de Los Santos, un viejo favorito de los fans, GTA San Andreas. 561 vehículos diferentes (incluidos todos los transportes que puede operar) y la cantidad aumenta con cada actualización.\\nNarración simultánea desde tres perspectivas únicas:\\nSigue a Michael, ex-criminal que vive su vida de ocio lejos del pasado, Franklin, un niño que busca un futuro mejor, y Trevor, el pasado exacto del que Michael está tratando de huir.\\nGTA Online proporcionará muchos desafíos adicionales incluso para los jugadores experimentados, recién llegados del modo historia. Ahora tendrás otros jugadores cerca que pueden ayudarte con la misma probabilidad que arruinar tu misión. Los jugadores pueden experimentar todas las mecánicas de GTA actualizadas a través del personaje personalizable único, y el contenido de la comunidad combinado con el sistema de nivelación tiende a mantener a todos ocupados y comprometidos.",
                    tags = listOf(),
                    backgroundImage = "doctus",
                    tba = false,
                    dominantColor = "ipsum",
                    name = "Grand Theft Auto V",
                    redditDescription = "eripuit",
                    redditLogo = "pertinacia",
                    updated = "neque",
                    reviewsCount = 1654,
                    metacritic = 9521,
                    description = "taciti",
                    metacriticUrl = "http://www.bing.com/search?q=constituam",
                    alternativeNames = listOf(),
                    parentsCount = 2472,
                    metacriticPlatforms = listOf(),
                    creatorsCount = 2756,
                    ratings = listOf(),
                    genres = listOf(),
                    saturatedColor = "dolor",
                    addedByStatus = AddedByStatus(),
                    redditUrl = "https://duckduckgo.com/?q=sapien",
                    redditCount = 8907,
                    parentAchievementsCount = 7574,
                    website = "adipiscing",
                    suggestionsCount = 4795,
                    stores = listOf(),
                    additionsCount = 8031,
                    twitchCount = 8218,
                    backgroundImageAdditional = "justo",
                    esrbRating = EsrbRating(),
                    screenshotsCount = 7463

                )
            ),
            gamesScreenshotsPaging = gamesScreenshotsPaging,
            onIntent = {}
        )
    }
}