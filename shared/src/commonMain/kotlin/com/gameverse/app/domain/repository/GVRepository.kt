package com.gameverse.app.domain.repository

import com.gameverse.app.data.response.GamesResponse

interface GVRepository {
    suspend fun getGames(): GamesResponse
}