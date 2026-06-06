package com.gameverse.app.presentation.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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

        Games(items = uiState.gamesData?.results)
    }
}

@Composable
private fun Games(
    modifier: Modifier = Modifier,
    items: List<GamesResponse.ResultsItem>?
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(items.orEmpty(), key = { it.id ?: 0 }) { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = GVShapes.large,
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
                        platforms = result.platforms.orEmpty()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = result.name.orEmpty(),
                        style = GVTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "View More",
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
    platforms: List<GamesResponse.ResultsItem.PlatformsItem>
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
                            platforms = listOf(
                                GamesResponse.ResultsItem.PlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 21
                                    )
                                ),
                                GamesResponse.ResultsItem.PlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 80
                                    )
                                ),
                                GamesResponse.ResultsItem.PlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 19
                                    )
                                ),
                                GamesResponse.ResultsItem.PlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 3
                                    )
                                ),
                                GamesResponse.ResultsItem.PlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 4
                                    )
                                ),
                                GamesResponse.ResultsItem.PlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 6
                                    )
                                ),
                                GamesResponse.ResultsItem.PlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 18
                                    )
                                ),
                                GamesResponse.ResultsItem.PlatformsItem(
                                    platform = GamesResponse.ResultsItem.Platform(
                                        id = 100
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