package com.gameverse.app.presentation.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainRoutes : NavKey {
    @Serializable
    data object Home : MainRoutes

    @Serializable
    data object Search : MainRoutes

    @Serializable
    data object Favorite : MainRoutes

    @Serializable
    data object Profile : MainRoutes
}