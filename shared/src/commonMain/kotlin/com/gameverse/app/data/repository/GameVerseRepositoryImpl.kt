package com.gameverse.app.data.repository

import com.gameverse.app.data.api.GameVerseService
import com.gameverse.app.data.response.GamesResponse
import com.gameverse.app.domain.repository.GameVerseRepository
import org.koin.core.annotation.Singleton

@Singleton(binds = [GameVerseRepository::class])
class GameVerseRepositoryImpl(private val apiService: GameVerseService) : GameVerseRepository {
    override suspend fun getGames(): GamesResponse = apiService.getGames()
}