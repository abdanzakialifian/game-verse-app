package com.gameverse.app.data.api

import com.gameverse.app.data.resources.Games
import com.gameverse.app.data.response.GamesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import org.koin.core.annotation.Singleton

@Singleton
class GameVerseService(private val client: HttpClient) {
    suspend fun getGames(): GamesResponse {
        return client.get(Games()).body<GamesResponse>()
    }
}