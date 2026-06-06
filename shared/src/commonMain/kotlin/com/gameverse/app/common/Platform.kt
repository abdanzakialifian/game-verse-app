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

enum class Platform(val icon: DrawableResource, vararg val ids: Int) {
    PLAYSTATION(Res.drawable.ic_playstation, 27, 15, 16, 19, 187),
    XBOX(Res.drawable.ic_xbox, 1, 14, 80, 186),
    ANDROID(Res.drawable.ic_android, 21),
    APPLE(Res.drawable.ic_apple, 3, 41, 51),
    WINDOWS(Res.drawable.ic_windows, 4),
    LINUX(Res.drawable.ic_linux, 6),
    NINTENDO(Res.drawable.ic_nintendo, 7, 8, 9, 13, 17, 18, 83),
    WEB(Res.drawable.ic_web);

    companion object {
        fun iconFromIds(ids: List<Int>): List<DrawableResource> = ids.map { id ->
            entries.find { id in it.ids }?.icon ?: WEB.icon
        }.distinct()
    }
}