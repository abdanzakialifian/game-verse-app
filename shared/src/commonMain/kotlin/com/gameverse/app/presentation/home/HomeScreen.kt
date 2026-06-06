package com.gameverse.app.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gameverse.app.common.Platform
import com.gameverse.app.common.formatDate
import com.gameverse.app.component.GVSearch
import com.gameverse.app.data.response.GamesResponse
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.collections.map
import kotlin.collections.orEmpty
import kotlin.text.orEmpty

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(HomeReducer.Intent.OnGetGames)
    }

    HomeContent(
        uiState = uiState,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun HomeContent(
    uiState: HomeReducer.State,
    onIntent: (HomeReducer.Intent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        GVSearch(
            hint = "Search games....",
            value = uiState.searchValue,
            onValueChange = { value ->
                onIntent(HomeReducer.Intent.OnSearchValueChanged(value))
            }
        )

        Games(
            items = uiState.gamesData?.results,
            isExpanded = uiState.isExpanded,
            onExpand = {
                onIntent(HomeReducer.Intent.OnExpanded(!uiState.isExpanded))
            }
        )
    }
}

@Composable
private fun Games(
    modifier: Modifier = Modifier,
    items: List<GamesResponse.ResultsItem>?,
    isExpanded: Boolean,
    onExpand: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(items.orEmpty(), key = { it.id ?: 0 }) { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = GVShapes.medium,
                colors = CardDefaults.cardColors(contentColor = GVColor.secondary)
            ) {
                Column {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth().height(130.dp),
                        model = result.backgroundImage,
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null,
                        filterQuality = FilterQuality.Medium,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Platforms(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        platforms = result.parentPlatforms.orEmpty()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = result.name.orEmpty(),
                        style = GVTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    )

                    MoreInformation(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        result = result,
                        isExpanded = isExpanded,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = onExpand
                            ),
                        text = if (isExpanded) "View less" else "View more",
                        style = GVTypography.labelMedium.copy(textDecoration = TextDecoration.Underline),
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun Platforms(
    modifier: Modifier = Modifier,
    platforms: List<GamesResponse.ResultsItem.ParentPlatformsItem>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val platformIds = platforms.map { it.platform?.id ?: 0 }
        val platformIcons = Platform.iconFromIds(platformIds)
        platformIcons.forEach { iconDrawable ->
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(iconDrawable),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
private fun MoreInformation(
    modifier: Modifier = Modifier,
    result: GamesResponse.ResultsItem,
    isExpanded: Boolean,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isExpanded,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Release date:",
                    style = GVTypography.labelSmall
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = result.released.orEmpty().formatDate(),
                    style = GVTypography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Genres:",
                    style = GVTypography.labelSmall
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = result.genres?.joinToString(", ") { it.name.orEmpty() }.orEmpty(),
                    style = GVTypography.labelSmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    GVTheme {
        HomeContent(
            uiState = HomeReducer.State(
                gamesData = GamesResponse(
                    results = List(5) {
                        GamesResponse.ResultsItem(
                            id = it,
                            backgroundImage = "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                            name = "Grand Theft Auto V",
                            released = "2013-09-17",
                            genres = listOf(
                                GamesResponse.ResultsItem.GenresItem(
                                    name = "Action"
                                ),
                                GamesResponse.ResultsItem.GenresItem(
                                    name = "RPG"
                                ),
                                GamesResponse.ResultsItem.GenresItem(
                                    name = "Shooter"
                                ),
                            ),
                            parentPlatforms = listOf(
                                GamesResponse.ResultsItem.ParentPlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 1
                                    )
                                ),
                                GamesResponse.ResultsItem.ParentPlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 2
                                    )
                                ),
                                GamesResponse.ResultsItem.ParentPlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 3
                                    )
                                ),
                                GamesResponse.ResultsItem.ParentPlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 4
                                    )
                                ),
                                GamesResponse.ResultsItem.ParentPlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 5
                                    )
                                ),
                                GamesResponse.ResultsItem.ParentPlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 6
                                    )
                                ),
                                GamesResponse.ResultsItem.ParentPlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 7
                                    )
                                ),
                                GamesResponse.ResultsItem.ParentPlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 8
                                    )
                                ),
                                GamesResponse.ResultsItem.ParentPlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 9
                                    )
                                )
                            )
                        )
                    }
                )
            ),
            onIntent = {}
        )
    }
}