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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gameverse.app.common.Greeting
import com.gameverse.app.common.LaunchEffectOnce
import com.gameverse.app.common.Utils
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.presentation.shared.GameItem
import com.gameverse.app.presentation.shared.GeneralEmpty
import com.gameverse.app.presentation.shared.GeneralError
import com.gameverse.app.presentation.shared.LoadingPlaceholders
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_profile
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToGameList: () -> Unit,
    onNavigateToGameSeries: (gamePk: String) -> Unit,
    onNavigateToDetail: (gamePk: String) -> Unit,
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
                is HomeReducer.Effect.NavigateToDetail -> onNavigateToDetail(effect.gamePk)
            }
        }
    }

    HomeContent(
        paddingValues = paddingValues,
        uiState = uiState,
        onIntent = viewModel::sendIntent
    )
}

@Composable
private fun HomeContent(
    uiState: HomeReducer.State,
    paddingValues: PaddingValues,
    onIntent: (HomeReducer.Intent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
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
            uiState.isGamesLoading -> LoadingPlaceholders()
            uiState.gamesError != null -> GeneralError(
                onButtonClicked = {
                    onIntent(HomeReducer.Intent.OnGetGames)
                }
            )
            else -> {
                if (uiState.gamesData.isEmpty()) {
                    GeneralEmpty()
                    return
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(uiState.gamesData, key = { it.id }) { result ->
                        GameItem(
                            game = result,
                            expandedIds = uiState.expandedIds,
                            onShowMoreClicked = { gamePk ->
                                onIntent(HomeReducer.Intent.OnNavigateToGameSeries(gamePk))
                            },
                            onExpand = { id ->
                                onIntent(HomeReducer.Intent.OnExpanded(id))
                            },
                            onItemClicked = { id ->
                                onIntent(HomeReducer.Intent.OnNavigateToDetail(id.toString()))
                            },
                        )
                    }
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
                genres = listOf("Action", "RPG", "Shooter"),
                parentPlatforms = (1..10).toList(),
            )
        }
        HomeContent(
            uiState = HomeReducer.State(
                expandedIds = gamesData.map { it.id }.toSet(),
                gamesData = gamesData
            ),
            paddingValues = PaddingValues(),
            onIntent = {}
        )
    }
}