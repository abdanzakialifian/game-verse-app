package com.gameverse.app.data.config

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.logging.Logger

object KtorLogger : Logger {
    override fun log(message: String) {
        Napier.v(message, null, "KtorHttpClient")
    }
}