package com.gameverse.app.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.gameverse.app.presentation.favorite.FavoriteScreen
import com.gameverse.app.presentation.home.HomeScreen
import com.gameverse.app.presentation.profile.ProfileScreen
import com.gameverse.app.presentation.search.SearchScreen
import com.gameverse.app.theme.GameVerseColor
import com.gameverse.app.theme.GameVerseTheme
import com.gameverse.app.theme.GameVerseTypography
import com.gameverse.app.theme.Lavender
import com.gameverse.app.theme.SoftViolet
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
                    subclass(MainRoutes.Search::class, MainRoutes.Search.serializer())
                    subclass(MainRoutes.Favorite::class, MainRoutes.Favorite.serializer())
                    subclass(MainRoutes.Profile::class, MainRoutes.Profile.serializer())
                }
            }
        },
        startDestination.route
    )

    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = GameVerseColor.background
            ) {
                NavBarDestination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painter = painterResource(destination.icon),
                                    contentDescription = destination.contentDescription,
                                    tint = if (selectedDestination == index) SoftViolet else Lavender
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = destination.label,
                                    style = GameVerseTypography.labelSmall,
                                    color = if (selectedDestination == index) SoftViolet else Lavender
                                )
                            }
                        },
                        onClick = {
                            backStack.add(destination.route)
                            selectedDestination = index
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<MainRoutes.Home> {
                    HomeScreen()
                }

                entry<MainRoutes.Search> {
                    SearchScreen()
                }

                entry<MainRoutes.Favorite> {
                    FavoriteScreen()
                }

                entry<MainRoutes.Profile> {
                    ProfileScreen()
                }
            }
        )
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
    GameVerseTheme {
        ProvideNavigationEventDispatcher {
            MainApp()
        }
    }
}