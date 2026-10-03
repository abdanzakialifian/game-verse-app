package com.gameverse.app.presentation.main

import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_catalogue
import gameverse.shared.generated.resources.ic_favorite
import gameverse.shared.generated.resources.ic_home
import gameverse.shared.generated.resources.ic_profile
import gameverse.shared.generated.resources.nav_catalogue
import gameverse.shared.generated.resources.nav_favorite
import gameverse.shared.generated.resources.nav_home
import gameverse.shared.generated.resources.nav_profile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class NavBarDestination(
    val route: MainRoutes,
    val label: StringResource,
    val icon: DrawableResource,
    val contentDescription: StringResource,
) {
    Home(MainRoutes.Home, Res.string.nav_home, Res.drawable.ic_home, Res.string.nav_home),
    Catalogue(MainRoutes.Catalogue, Res.string.nav_catalogue, Res.drawable.ic_catalogue, Res.string.nav_catalogue),
    Favorite(MainRoutes.Favorite, Res.string.nav_favorite, Res.drawable.ic_favorite, Res.string.nav_favorite),
    Profile(MainRoutes.Profile, Res.string.nav_profile, Res.drawable.ic_profile, Res.string.nav_profile),
}