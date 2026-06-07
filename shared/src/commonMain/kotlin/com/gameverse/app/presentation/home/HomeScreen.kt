package com.gameverse.app.presentation.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gameverse.app.component.GVSearch
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.presentation.shared.GameInformation
import com.gameverse.app.presentation.shared.GamePlaceholders
import com.gameverse.app.presentation.shared.GamePlatforms
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToGameList: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(HomeReducer.Intent.OnGetGames)
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when(effect) {
                HomeReducer.Effect.NavigateToGameList -> onNavigateToGameList()
            }
        }
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

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "For you",
                style = GVTypography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onIntent(HomeReducer.Intent.OnNavigateToGameList)
                    }
                ),
                text = "See all",
                style = GVTypography.labelSmall
            )
        }

        if (uiState.isGamesLoading) {
            GamePlaceholders()
        } else {
            Games(
                games = uiState.gamesData,
                onExpand = { id, isExpanded ->
                    onIntent(HomeReducer.Intent.OnExpanded(id, !isExpanded))
                }
            )
        }
    }
}

@Composable
private fun Games(
    modifier: Modifier = Modifier,
    games: List<GamesModel>,
    onExpand: (id: Int, isExpanded: Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(games, key = { it.id }) { result ->
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
                        isExpanded = result.isExpanded,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    onExpand(result.id, result.isExpanded)
                                }
                            ),
                        text = if (result.isExpanded) "View less" else "View more",
                        style = GVTypography.labelMedium.copy(textDecoration = TextDecoration.Underline),
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
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
                gamesData = List(5) {
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
                        isExpanded = true
                    )
                }
            ),
            onIntent = {}
        )
    }
}