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
import androidx.compose.material3.Icon
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
import com.gameverse.app.common.Greeting
import com.gameverse.app.common.LaunchEffectOnce
import com.gameverse.app.common.Utils
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.presentation.shared.GeneralError
import com.gameverse.app.presentation.shared.GameInformation
import com.gameverse.app.presentation.shared.GamePlaceholders
import com.gameverse.app.presentation.shared.GamePlatforms
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_profile
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToGameList: () -> Unit,
    onNavigateToGameSeries: (gamePk: String) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchEffectOnce(Unit) {
        viewModel.sendIntent(HomeReducer.Intent.OnGetGames)
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeReducer.Effect.NavigateToGameList -> onNavigateToGameList()
                is HomeReducer.Effect.NavigateToGameSeries -> onNavigateToGameSeries(effect.gamePk)
            }
        }
    }

    HomeContent(
        modifier = modifier,
        uiState = uiState,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun HomeContent(
    uiState: HomeReducer.State,
    modifier: Modifier = Modifier,
    onIntent: (HomeReducer.Intent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_profile),
                tint = GVColor.onSurfaceVariant,
                contentDescription = null,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = when (Utils.getGreeting()) {
                        Greeting.MORNING -> "Good morning,"
                        Greeting.AFTERNOON -> "Good afternoon,"
                        Greeting.EVENING -> "Good evening,"
                        Greeting.NIGHT -> "Good night,"
                    },
                    style = GVTypography.titleSmall
                )

                Text(
                    text = "Abdan Zaki Alifian",
                    style = GVTypography.bodySmall
                )
            }
        }

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

        when {
            uiState.isGamesLoading -> GamePlaceholders()
            uiState.gamesError != null -> GeneralError(
                onButtonClicked = {
                    onIntent(HomeReducer.Intent.OnGetGames)
                }
            )
            else -> Games(
                expandedIds = uiState.expandedIds,
                games = uiState.gamesData,
                onExpand = { id ->
                    onIntent(HomeReducer.Intent.OnExpanded(id))
                },
                onShowMoreClicked = { gamePk ->
                    onIntent(HomeReducer.Intent.OnNavigateToGameSeries(gamePk))
                }
            )
        }
    }
}

@Composable
private fun Games(
    expandedIds: Set<Int>,
    games: List<GamesModel>,
    modifier: Modifier = Modifier,
    onExpand: (id: Int) -> Unit,
    onShowMoreClicked: (gamePk: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(games, key = { it.id }) { result ->
            val isExpanded = result.id in expandedIds

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
                        isExpanded = isExpanded,
                        onButtonClicked = {
                            onShowMoreClicked(result.id.toString())
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
                        text = if (isExpanded) "View less" else "View more",
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
        HomeContent(
            uiState = HomeReducer.State(
                expandedIds = gamesData.map { it.id }.toSet(),
                gamesData = gamesData
            ),
            onIntent = {}
        )
    }
}