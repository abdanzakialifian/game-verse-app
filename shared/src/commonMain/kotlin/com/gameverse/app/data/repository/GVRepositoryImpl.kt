package com.gameverse.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.gameverse.app.data.api.GVService
import com.gameverse.app.data.dao.GVFavoriteDao
import com.gameverse.app.data.entity.FavoriteEntity
import com.gameverse.app.data.mapper.toDomain
import com.gameverse.app.data.paging.GamesPagingSource
import com.gameverse.app.data.paging.ScreenshotsPagingSource
import com.gameverse.app.data.paging.SeriesPagingSource
import com.gameverse.app.data.paging.GenresPagingSource
import com.gameverse.app.di.IoDispatcher
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.GameModel
import com.gameverse.app.domain.model.GenreModel
import com.gameverse.app.domain.model.ScreenshotModel
import com.gameverse.app.domain.repository.GVRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton(binds = [GVRepository::class])
class GVRepositoryImpl(
    private val apiService: GVService,
    private val favoriteDao: GVFavoriteDao,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
) : GVRepository {
    override suspend fun getGames(): List<GameModel> = withContext(dispatcher) {
        apiService.getGames().results?.toDomain().orEmpty()
    }

    override fun getGameListPaging(query: String?, genres: String?): Flow<PagingData<GameModel>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = {
                GamesPagingSource(
                    query = query,
                    genres = genres,
                    apiService = apiService
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { result ->
                result.toDomain()
            }
        }

    override fun getSeriesPaging(gamePk: String): Flow<PagingData<GameModel>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE
        ),
        pagingSourceFactory = {
            SeriesPagingSource(
                gamePk = gamePk,
                apiService = apiService
            )
        }
    ).flow.map { pagingData ->
        pagingData.map { result ->
            result.toDomain()
        }
    }

    override fun getGenresPaging(): Flow<PagingData<GenreModel>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE
        ),
        pagingSourceFactory = {
            GenresPagingSource(apiService)
        }
    ).flow.map { pagingData ->
        pagingData.map { result ->
            result.toDomain()
        }
    }

    override suspend fun getGameDetail(id: String): DetailModel = withContext(dispatcher) {
        apiService.getDetail(id).toDomain()
    }

    override fun getScreenshotsPaging(gamePK: String): Flow<PagingData<ScreenshotModel>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE
        ),
        pagingSourceFactory = {
            ScreenshotsPagingSource(
                apiService = apiService,
                gamePk = gamePK
            )
        }
    ).flow.map { pagingData ->
        pagingData.map { result ->
            result.toDomain()
        }
    }

    override suspend fun getMovies(id: String): List<String> = withContext(dispatcher) {
        apiService.getMovies(id).results?.map {
            it.name.orEmpty()
        }.orEmpty()
    }

    override suspend fun saveFavorite(favoriteEntity: FavoriteEntity) {
        favoriteDao.insert(favoriteEntity)
    }

    override suspend fun deleteFavoriteById(id: Int) {
        favoriteDao.deleteById(id)
    }

    override fun getFavorites(): Flow<List<GameModel>> {
        return favoriteDao.getFavorites().map { favoriteEntities ->
            favoriteEntities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override fun getFavoriteStatus(id: Int): Flow<Boolean> = favoriteDao.getFavoriteStatus(id)

    companion object {
        private const val PAGE_SIZE = 10
        private const val PREFETCH_DISTANCE = 3
    }
}