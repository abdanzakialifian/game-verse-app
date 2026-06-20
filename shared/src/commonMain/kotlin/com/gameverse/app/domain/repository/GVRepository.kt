package com.gameverse.app.domain.repository

import androidx.paging.PagingData
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.domain.model.GenresModel
import kotlinx.coroutines.flow.Flow

interface GVRepository {
    suspend fun getGames(): List<GamesModel>
    fun getGamesPaging(query: String?, genres: String?): Flow<PagingData<GamesModel>>
    fun getGamesSeriesPaging(gamePk: String): Flow<PagingData<GamesModel>>
    fun getGenresPaging(): Flow<PagingData<GenresModel>>
}