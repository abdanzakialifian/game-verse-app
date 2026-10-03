package com.gameverse.app.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gameverse.app.common.DayPeriod
import com.gameverse.app.common.Utils
import com.gameverse.app.domain.model.GameModel
import com.gameverse.app.presentation.shared.GameListItem
import com.gameverse.app.presentation.shared.GeneralEmpty
import com.gameverse.app.presentation.shared.GeneralError
import com.gameverse.app.presentation.shared.LoadingPlaceholders
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVShapes
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.greeting_afternoon
import gameverse.shared.generated.resources.greeting_evening
import gameverse.shared.generated.resources.greeting_morning
import gameverse.shared.generated.resources.greeting_night
import gameverse.shared.generated.resources.home_view_all_games
import gameverse.shared.generated.resources.ic_profile
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = koinViewModel(),
    onShowGameList: () -> Unit,
    onShowGameSeries: (gamePk: String) -> Unit,
    onShowDetail: (gamePk: String) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeReducer.Effect.ShowGameList -> onShowGameList()
                is HomeReducer.Effect.ShowGameSeries -> onShowGameSeries(effect.gamePk)
                is HomeReducer.Effect.ShowDetail -> onShowDetail(effect.gamePk)
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
                    text = when (Utils.getDayPeriod()) {
                        DayPeriod.MORNING -> stringResource(Res.string.greeting_morning)
                        DayPeriod.AFTERNOON -> stringResource(Res.string.greeting_afternoon)
                        DayPeriod.EVENING -> stringResource(Res.string.greeting_evening)
                        DayPeriod.NIGHT -> stringResource(Res.string.greeting_night)
                    },
                    style = GVTypography.titleSmall
                )

                Text(
                    text = "Abdan Zaki Alifian",
                    style = GVTypography.bodySmall
                )
            }
        }

        when {
            uiState.isLoading -> LoadingPlaceholders(modifier = Modifier.weight(1f))
            uiState.error != null -> GeneralError(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp),
                onButtonClicked = {
                    onIntent(HomeReducer.Intent.LoadGames)
                }
            )

            else -> {
                if (uiState.games.isEmpty()) {
                    GeneralEmpty(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 16.dp)
                    )
                    return
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.games, key = { it.id }) { result ->
                        GameListItem(
                            game = result,
                            expandedIds = uiState.expandedIds,
                            onShowMoreClicked = { gamePk ->
                                onIntent(HomeReducer.Intent.NavigateToGameSeries(gamePk))
                            },
                            onExpand = { id ->
                                onIntent(HomeReducer.Intent.Expand(id))
                            },
                            onItemClicked = { id ->
                                onIntent(HomeReducer.Intent.NavigateToDetail(id.toString()))
                            },
                        )
                    }

                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = GVShapes.small,
                            colors = ButtonDefaults.buttonColors(containerColor = GVColor.outline),
                            onClick = {
                                onIntent(HomeReducer.Intent.NavigateToGameList)
                            }
                        ) {
                            Text(
                                text = stringResource(Res.string.home_view_all_games),
                                style = GVTypography.labelSmall
                            )
                        }
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
            GameModel(
                id = it,
                name = "Grand Theft Auto V",
                backgroundImage = "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                released = "2013-09-17",
                genreNames = listOf("Action", "RPG", "Shooter"),
                platformIds = (1..10).toList(),
            )
        }
        HomeContent(
            uiState = HomeReducer.State(
                expandedIds = gamesData.map { it.id }.toSet(),
                games = gamesData
            ),
            paddingValues = PaddingValues(),
            onIntent = {}
        )
    }
}