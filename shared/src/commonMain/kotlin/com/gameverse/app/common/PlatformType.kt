package com.gameverse.app.common

import gameverse.shared.generated.resources.Res
import gameverse.shared.generated.resources.ic_android
import gameverse.shared.generated.resources.ic_apple
import gameverse.shared.generated.resources.ic_linux
import gameverse.shared.generated.resources.ic_nintendo
import gameverse.shared.generated.resources.ic_playstation
import gameverse.shared.generated.resources.ic_web
import gameverse.shared.generated.resources.ic_windows
import gameverse.shared.generated.resources.ic_xbox
import org.jetbrains.compose.resources.DrawableResource

enum class PlatformType(val icon: DrawableResource, val id: Int? = null) {
    WINDOWS(Res.drawable.ic_windows, 1),
    PLAYSTATION(Res.drawable.ic_playstation, 2),
    XBOX(Res.drawable.ic_xbox, 3),
    APPLE(Res.drawable.ic_apple, 4),
    APPLE_MAC(Res.drawable.ic_apple, 5),
    LINUX(Res.drawable.ic_linux, 6),
    NINTENDO(Res.drawable.ic_nintendo, 7),
    ANDROID(Res.drawable.ic_android, 8),
    WEB(Res.drawable.ic_web);

    companion object {
        fun iconFromIds(ids: List<Int>): List<DrawableResource> = ids.map { id ->
            entries.find { id == it.id }?.icon ?: WEB.icon
        }.distinct()
    }
}