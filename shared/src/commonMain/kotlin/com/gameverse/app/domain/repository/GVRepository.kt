package com.gameverse.app.domain.repository

import androidx.paging.PagingData
import com.gameverse.app.data.entity.FavoriteEntity
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.GameModel
import com.gameverse.app.domain.model.GenreModel
import com.gameverse.app.domain.model.ScreenshotModel
import kotlinx.coroutines.flow.Flow

interface GVRepository {
    suspend fun getGames(): List<GameModel>
    fun getGamesPaging(query: String?, genres: String?): Flow<PagingData<GameModel>>
    fun getGamesSeriesPaging(gamePk: String): Flow<PagingData<GameModel>>
    fun getGenresPaging(): Flow<PagingData<GenreModel>>
    suspend fun getGameDetail(id: String): DetailModel
    fun getGamesScreenshotsPaging(gamePK: String): Flow<PagingData<ScreenshotModel>>
    suspend fun getMoviesGames(id: String): List<String>
    suspend fun saveFavorite(favoriteEntity: FavoriteEntity)
    suspend fun deleteFavoriteById(id: Int)
    fun getFavorites(): Flow<List<GameModel>>
    fun getFavoriteStatus(id: Int): Flow<Boolean>
}