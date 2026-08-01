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
    fun getDayPeriod(): DayPeriod {
        val hour = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .hour

        return when (hour) {
            in 5..11 -> DayPeriod.MORNING
            in 12..16 -> DayPeriod.AFTERNOON
            in 17..20 -> DayPeriod.EVENING
            else -> DayPeriod.NIGHT
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