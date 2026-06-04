package com.gameverse.app.presentation.main

import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_catalogue
import gameverse.shared.generated.resources.ic_favorite
import gameverse.shared.generated.resources.ic_home
import gameverse.shared.generated.resources.ic_profile
import org.jetbrains.compose.resources.DrawableResource

enum class NavBarDestination(
    val route: MainRoutes,
    val label: String,
    val icon: DrawableResource,
    val contentDescription: String,
) {
    Home(MainRoutes.Home, "Home", Res.drawable.ic_home, "home"),
    Catalogue(MainRoutes.Catalogue, "Catalogue", Res.drawable.ic_catalogue, "catalogue"),
    Favorite(MainRoutes.Favorite, "Favorite", Res.drawable.ic_favorite, "favorite"),
    Profile(MainRoutes.Profile, "Profile", Res.drawable.ic_profile, "profile"),
}