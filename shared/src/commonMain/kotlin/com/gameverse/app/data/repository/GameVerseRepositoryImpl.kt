package com.gameverse.app.data.repository

import com.gameverse.app.data.api.GameVerseService
import com.gameverse.app.data.response.GamesResponse
import com.gameverse.app.di.IoDispatcher
import com.gameverse.app.domain.repository.GameVerseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton(binds = [GameVerseRepository::class])
class GameVerseRepositoryImpl(
    private val apiService: GameVerseService,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
) : GameVerseRepository {
    override suspend fun getGames(): GamesResponse = withContext(dispatcher) {
        apiService.getGames()
    }
}