package com.gameverse.app.data.api

import com.gameverse.app.data.resources.Games
import com.gameverse.app.data.resources.Genres
import com.gameverse.app.data.response.BaseListResponse
import com.gameverse.app.data.response.GameDetailResponse
import com.gameverse.app.data.response.GamesItemResponse
import com.gameverse.app.data.response.GamesMoviesItemResponse
import com.gameverse.app.data.response.GamesScreenshotsItemResponse
import com.gameverse.app.data.response.GenresItemResponse
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
    ): BaseListResponse<GamesItemResponse> {
        return client.get(
            Games(
                search = query,
                genres = genres,
                page = page,
                pageSize = pageSize
            )
        ).body<BaseListResponse<GamesItemResponse>>()
    }

    suspend fun getGamesSeries(
        gamePk: String,
        page: Int,
        pageSize: Int,
    ): BaseListResponse<GamesItemResponse> {
        return client.get(
            Games.Series(
                gamePk = gamePk,
                page = page,
                pageSize = pageSize
            )
        ).body<BaseListResponse<GamesItemResponse>>()
    }

    suspend fun getGenres(
        page: Int,
        pageSize: Int,
    ): BaseListResponse<GenresItemResponse> {
        return client.get(
            Genres(
                page = page,
                pageSize = pageSize
            )
        ).body<BaseListResponse<GenresItemResponse>>()
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
    ): BaseListResponse<GamesScreenshotsItemResponse> {
        return client.get(
            Games.Screenshots(
                gamePk = gamePk,
                page = page,
                pageSize = pageSize
            )
        ).body<BaseListResponse<GamesScreenshotsItemResponse>>()
    }

    suspend fun getGamesMovies(id: String): BaseListResponse<GamesMoviesItemResponse> {
        return client.get(Games.Movies(id)).body<BaseListResponse<GamesMoviesItemResponse>>()
    }
}