package com.gameverse.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.gameverse.app.common.Constants
import com.gameverse.app.data.api.GVService
import com.gameverse.app.data.mapper.toDomain
import com.gameverse.app.data.paging.GamesPagingSource
import com.gameverse.app.data.paging.GamesSeriesPagingSource
import com.gameverse.app.data.paging.GenresPagingSource
import com.gameverse.app.di.IoDispatcher
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.domain.model.GenresModel
import com.gameverse.app.domain.repository.GVRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton(binds = [GVRepository::class])
class GVRepositoryImpl(
    private val apiService: GVService,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
) : GVRepository {
    override suspend fun getGames(): List<GamesModel> = withContext(dispatcher) {
        apiService.getGames().toDomain()
    }

    override fun getGamesPaging(query: String?, genres: String?): Flow<PagingData<GamesModel>> =
        Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = Constants.PAGE_SIZE,
                prefetchDistance = Constants.PREFETCH_DISTANCE
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

    override fun getGamesSeriesPaging(gamePk: String): Flow<PagingData<GamesModel>> = Pager(
        config = PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = Constants.PAGE_SIZE,
            prefetchDistance = Constants.PREFETCH_DISTANCE
        ),
        pagingSourceFactory = {
            GamesSeriesPagingSource(
                gamePk = gamePk,
                apiService = apiService
            )
        }
    ).flow.map { pagingData ->
        pagingData.map { result ->
            result.toDomain()
        }
    }

    override fun getGenresPaging(): Flow<PagingData<GenresModel>> = Pager(
        config = PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = Constants.PAGE_SIZE,
            prefetchDistance = Constants.PREFETCH_DISTANCE
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
        apiService.getGameDetail(id).toDomain()
    }
}