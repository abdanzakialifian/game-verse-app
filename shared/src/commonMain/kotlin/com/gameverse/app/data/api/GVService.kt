package com.gameverse.app.data.api

import com.gameverse.app.data.resources.Games
import com.gameverse.app.data.response.BaseListResponse
import com.gameverse.app.data.response.GameDetailResponse
import com.gameverse.app.data.response.GameItemResponse
import com.gameverse.app.data.response.MovieItemResponse
import com.gameverse.app.data.response.ScreenshotsItemResponse
import com.gameverse.app.data.response.GenreItemResponse
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
    ): BaseListResponse<GameItemResponse> {
        return client.get(
            Games(
                search = query,
                genres = genres,
                page = page,
                pageSize = pageSize
            )
        ).body<BaseListResponse<GameItemResponse>>()
    }

    suspend fun getSeries(
        gamePk: String,
        page: Int,
        pageSize: Int,
    ): BaseListResponse<GameItemResponse> {
        return client.get(
            Games.Series(
                gamePk = gamePk,
                page = page,
                pageSize = pageSize
            )
        ).body<BaseListResponse<GameItemResponse>>()
    }

    suspend fun getGenres(
        page: Int,
        pageSize: Int,
    ): BaseListResponse<GenreItemResponse> {
        return client.get(
            Games.Genres(
                page = page,
                pageSize = pageSize
            )
        ).body<BaseListResponse<GenreItemResponse>>()
    }

    suspend fun getDetail(id: String): GameDetailResponse {
        return client.get(
            Games.Detail(id = id)
        ).body<GameDetailResponse>()
    }

    suspend fun getScreenshots(
        gamePk: String,
        page: Int,
        pageSize: Int,
    ): BaseListResponse<ScreenshotsItemResponse> {
        return client.get(
            Games.Screenshots(
                gamePk = gamePk,
                page = page,
                pageSize = pageSize
            )
        ).body<BaseListResponse<ScreenshotsItemResponse>>()
    }

    suspend fun getMovies(id: String): BaseListResponse<MovieItemResponse> {
        return client.get(Games.Movies(id)).body<BaseListResponse<MovieItemResponse>>()
    }
}