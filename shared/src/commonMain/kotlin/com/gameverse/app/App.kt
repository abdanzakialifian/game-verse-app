package com.gameverse.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.gameverse.app.theme.GameVerseTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun App() {
    GameVerseTheme {
        NavGraph()
    }
}

@Serializable
private sealed interface Routes : NavKey {
    @Serializable
    data object RoutesA : Routes

    @Serializable
    data object RoutesB : Routes

    @Serializable
    data object RoutesC : Routes
}

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Routes.RoutesA::class, Routes.RoutesA.serializer())
            subclass(Routes.RoutesB::class, Routes.RoutesB.serializer())
            subclass(Routes.RoutesC::class, Routes.RoutesC.serializer())
        }
    }
}

@Composable
private fun NavGraph() {
    val backStack = rememberNavBackStack(config, Routes.RoutesA)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Routes.RoutesA> {
                ScreenA(
                    onNext = {
                        backStack.add(Routes.RoutesB)
                    }
                )
            }
            entry<Routes.RoutesB> {
                ScreenB(
                    onNext = {
                        backStack.add(Routes.RoutesC)
                    },
                    onBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }
            entry<Routes.RoutesC> {
                ScreenC(
                    onBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}

@Composable
private fun ScreenA(
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Screen A")
        Button(
            onClick = onNext,
            content = {
                Text("Next")
            }
        )
    }
}

@Composable
private fun ScreenB(
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Screen B")
        Button(
            onClick = onNext,
            content = {
                Text("Next")
            }
        )
        Button(
            onClick = onBack,
            content = {
                Text("Back")
            }
        )
    }
}

@Composable
private fun ScreenC(
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Screen C")
        Button(
            onClick = onBack,
            content = {
                Text("Back")
            }
        )
    }
}