package com.gameverse.app.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.savedstate.serialization.SavedStateConfiguration
import com.gameverse.app.common.Utils
import com.gameverse.app.presentation.catalogue.CatalogueScreen
import com.gameverse.app.presentation.detail.DetailScreen
import com.gameverse.app.presentation.favorite.FavoriteScreen
import com.gameverse.app.presentation.games.list.GameListScreen
import com.gameverse.app.presentation.games.series.GameSeriesScreen
import com.gameverse.app.presentation.home.HomeScreen
import com.gameverse.app.presentation.profile.ProfileScreen
import com.gameverse.app.theme.GVColor
import com.gameverse.app.theme.GVTheme
import com.gameverse.app.theme.GVTypography
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainApp() {
    val startDestination = NavBarDestination.Home

    val backStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(MainRoutes.Home::class, MainRoutes.Home.serializer())
                    subclass(MainRoutes.Catalogue::class, MainRoutes.Catalogue.serializer())
                    subclass(MainRoutes.Favorite::class, MainRoutes.Favorite.serializer())
                    subclass(MainRoutes.Profile::class, MainRoutes.Profile.serializer())
                    subclass(MainRoutes.GameList::class, MainRoutes.GameList.serializer())
                    subclass(MainRoutes.GameSeries::class, MainRoutes.GameSeries.serializer())
                    subclass(MainRoutes.Detail::class, MainRoutes.Detail.serializer())
                }
            }
        },
        startDestination.route
    )

    val currentRoute = backStack.lastOrNull()

    Scaffold(
        bottomBar = {
            val isShowBottomBar = currentRoute in NavBarDestination.entries.map { it.route }
            MainBottomBar(
                isVisible = isShowBottomBar,
                selectedDestination = currentRoute,
                onBottomBarClicked = { route ->
                    backStack.add(route)
                }
            )
        }
    ) { innerPadding ->
        SharedTransitionLayout {
            NavDisplay(
                modifier = Modifier.fillMaxSize(),
                backStack = backStack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                sharedTransitionScope = this,
                entryProvider = entryProvider {
                    entry<MainRoutes.Home> {
                        HomeScreen(
                            paddingValues = innerPadding,
                            onNavigateToGameList = {
                                backStack.add(MainRoutes.GameList())
                            },
                            onNavigateToGameSeries = { gamePk ->
                                backStack.add(MainRoutes.GameSeries(gamePk))
                            },
                            onNavigateToDetail = { gamePk ->
                                backStack.add(MainRoutes.Detail(gamePk))
                            }
                        )
                    }

                    entry<MainRoutes.Catalogue> {
                        CatalogueScreen(
                            paddingValues = innerPadding,
                            onGenresClicked = { id ->
                                backStack.add(MainRoutes.GameList(id))
                            }
                        )
                    }

                    entry<MainRoutes.Favorite> {
                        FavoriteScreen(
                            paddingValues = innerPadding,
                            onNavigateToGameSeries = { gamePk ->
                                backStack.add(MainRoutes.GameSeries(gamePk))
                            },
                            onNavigateToDetail = { gamePk ->
                                backStack.add(MainRoutes.Detail(gamePk))
                            }
                        )
                    }

                    entry<MainRoutes.Profile> {
                        ProfileScreen()
                    }

                    entry<MainRoutes.GameList>(metadata = Utils.slideAnimation()) {
                        GameListScreen(
                            genreId = it.genreId,
                            onNavigateToGameSeries = { gamePk ->
                                backStack.add(MainRoutes.GameSeries(gamePk))
                            },
                            onNavigateToDetailGame = { id ->
                                backStack.add(MainRoutes.Detail(id.toString()))
                            },
                            onNavigateBack = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }

                    entry<MainRoutes.GameSeries>(metadata = Utils.slideAnimation()) {
                        GameSeriesScreen(
                            gamePk = it.gamePk,
                            onNavigateBack = {
                                backStack.removeLastOrNull()
                            },
                            onNavigateToDetailGame = { id ->
                                backStack.add(MainRoutes.Detail(id.toString()))
                            }
                        )
                    }

                    entry<MainRoutes.Detail>(metadata = Utils.slideAnimation()) {
                        DetailScreen(
                            gameId = it.gameId,
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun MainBottomBar(
    isVisible: Boolean,
    selectedDestination: NavKey?,
    onBottomBarClicked: (route: MainRoutes) -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        NavigationBar(
            containerColor = GVColor.background
        ) {
            NavBarDestination.entries.forEach { destination ->
                NavigationBarItem(
                    selected = selectedDestination == destination.route,
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(destination.icon),
                                contentDescription = destination.contentDescription,
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = destination.label,
                                style = GVTypography.labelSmall,
                            )
                        }
                    },
                    onClick = {
                        onBottomBarClicked(destination.route)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProvideNavigationEventDispatcher(
    content: @Composable () -> Unit
) {
    val dispatcher = remember { NavigationEventDispatcher() }

    val owner = remember {
        object : NavigationEventDispatcherOwner {
            override val navigationEventDispatcher: NavigationEventDispatcher
                get() = dispatcher
        }
    }

    CompositionLocalProvider(
        LocalNavigationEventDispatcherOwner provides owner,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun MainAppPreview() {
    GVTheme {
        ProvideNavigationEventDispatcher {
            MainApp()
        }
    }
}