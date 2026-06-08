package com.gameverse.app.common

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
}