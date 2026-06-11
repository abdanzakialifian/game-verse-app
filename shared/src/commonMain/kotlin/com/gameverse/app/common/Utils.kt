package com.gameverse.app.common

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

object Utils {
    fun getGreeting(): Greeting {
        val hour = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .hour

        return when (hour) {
            in 5..11 -> Greeting.MORNING
            in 12..16 -> Greeting.AFTERNOON
            in 17..20 -> Greeting.EVENING
            else -> Greeting.NIGHT
        }
    }

    fun slideAnimation(): Map<String, Any> = metadata {
        put(NavDisplay.TransitionKey) {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        }

        put(NavDisplay.PopTransitionKey) {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }

        put(NavDisplay.PredictivePopTransitionKey) {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }
    }
}