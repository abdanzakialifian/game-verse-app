package com.gameverse.app.domain.repository

import androidx.paging.PagingData
import com.gameverse.app.domain.model.GamesModel
import kotlinx.coroutines.flow.Flow

interface GVRepository {
    suspend fun getGames(): List<GamesModel>
    fun getGamesPaging(query: String): Flow<PagingData<GamesModel>>
}