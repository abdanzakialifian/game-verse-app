package com.gameverse.app.data.api

import com.gameverse.app.data.resources.Games
import com.gameverse.app.data.resources.Genres
import com.gameverse.app.data.response.GameDetailResponse
import com.gameverse.app.data.response.GamesResponse
import com.gameverse.app.data.response.GamesScreenshotsResponse
import com.gameverse.app.data.response.GenresResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import org.koin.core.annotation.Singleton

@Singleton
class GVService(private val client: HttpClient) {
    suspend fun getGames(
        query: String? = null,
        genres: String? = null,
        page: Int? = null,
        pageSize: Int? = null,
    ): GamesResponse {
        return client.get(
            Games(
                search = query,
                genres = genres,
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

    suspend fun getGenres(
        page: Int,
        pageSize: Int,
    ): GenresResponse {
        return client.get(
            Genres(
                page = page,
                pageSize = pageSize
            )
        ).body<GenresResponse>()
    }

    suspend fun getGameDetail(id: String): GameDetailResponse {
        return client.get(
            Games.Detail(id = id)
        ).body<GameDetailResponse>()
    }

    suspend fun getGamesScreenshots(
        gamePk: String,
        page: Int,
        pageSize: Int,
    ): GamesScreenshotsResponse {
        return client.get(
            Games.Screenshots(
                gamePk = gamePk,
                page = page,
                pageSize = pageSize
            )
        ).body<GamesScreenshotsResponse>()
    }
}