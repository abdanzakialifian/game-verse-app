package com.gameverse.app.data.api

import com.gameverse.app.data.resources.Games
import com.gameverse.app.data.response.GamesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import org.koin.core.annotation.Singleton

@Singleton
class GVService(private val client: HttpClient) {
    suspend fun getGames(
        query: String? = null,
        page: Int? = null,
        pageSize: Int? = null,
    ): GamesResponse {
        return client.get(
            Games(
                search = query,
                page = page,
                pageSize = pageSize
            )
        ).body<GamesResponse>()
    }

    suspend fun getGamesSeries(
        gamePk: String,
        page: Int,
        pageSize: Int,
    ): GamesResponse {
        return client.get(
            Games.Series(
                gamePk = gamePk,
                page = page,
                pageSize = pageSize
            )
        ).body<GamesResponse>()
    }
}