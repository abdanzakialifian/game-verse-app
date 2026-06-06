package com.gameverse.app.data.repository

import com.gameverse.app.data.api.GVService
import com.gameverse.app.data.response.GamesResponse
import com.gameverse.app.di.IoDispatcher
import com.gameverse.app.domain.repository.GVRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton(binds = [GVRepository::class])
class GVRepositoryImpl(
    private val apiService: GVService,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
) : GVRepository {
    override suspend fun getGames(): GamesResponse = withContext(dispatcher) {
        apiService.getGames()
    }
}