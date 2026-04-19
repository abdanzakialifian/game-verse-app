package com.gameverse.app.data.config

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun HttpClientConfig<*>.defaultConfig() {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.rawg.io"
            path("api/")
            parameters.append("key", "")
        }
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }

    install(Resources)

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(Logging) {
        logger = KtorLogger
        level = LogLevel.ALL
    }.also { Napier.base(DebugAntilog()) }
}