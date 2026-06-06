package com.gameverse.app.domain.repository

import com.gameverse.app.domain.model.GamesModel

interface GVRepository {
    suspend fun getGames(): List<GamesModel>
}