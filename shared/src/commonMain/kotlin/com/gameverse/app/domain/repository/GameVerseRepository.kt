package com.gameverse.app.domain.repository

import com.gameverse.app.data.response.GamesResponse

interface GameVerseRepository {
    suspend fun getGames(): GamesResponse
}