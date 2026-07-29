package com.gameverse.app.domain.repository

import androidx.paging.PagingData
import com.gameverse.app.data.entity.FavoriteEntity
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.domain.model.GenresModel
import com.gameverse.app.domain.model.MoviesModel
import com.gameverse.app.domain.model.ScreenshotsModel
import kotlinx.coroutines.flow.Flow

interface GVRepository {
    suspend fun getGames(): List<GamesModel>
    fun getGamesPaging(query: String?, genres: String?): Flow<PagingData<GamesModel>>
    fun getGamesSeriesPaging(gamePk: String): Flow<PagingData<GamesModel>>
    fun getGenresPaging(): Flow<PagingData<GenresModel>>
    suspend fun getGameDetail(id: String): DetailModel
    fun getGamesScreenshotsPaging(gamePK: String): Flow<PagingData<ScreenshotsModel>>
    suspend fun getMoviesGames(id: String): List<MoviesModel>
    suspend fun saveFavorite(favoriteEntity: FavoriteEntity)
    suspend fun deleteFavoriteById(id: Int)
    fun getFavorites(): Flow<List<GamesModel>>
    fun getFavoriteStatus(id: Int): Flow<Boolean>
}