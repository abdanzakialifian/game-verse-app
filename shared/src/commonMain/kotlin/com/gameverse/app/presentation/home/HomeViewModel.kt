package com.gameverse.app.presentation.home

import androidx.lifecycle.viewModelScope
import com.gameverse.app.domain.repository.GVRepository
import com.gameverse.app.mvi.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val repository: GVRepository
) : BaseViewModel<HomeReducer.State, HomeReducer.Event, HomeReducer.Effect, HomeReducer.Intent>(
    initialState = HomeReducer.State(),
    reducer = HomeReducer()
) {
    init {
        getGames()
    }

    override fun sendIntent(intent: HomeReducer.Intent) {
        when (intent) {
            is HomeReducer.Intent.Search -> sendEvent(
                HomeReducer.Event.SearchValueChanged(intent.value)
            )

            HomeReducer.Intent.LoadGames -> getGames()
            is HomeReducer.Intent.Expand -> sendEvent(
                HomeReducer.Event.Expanded(intent.id)
            )

            HomeReducer.Intent.NavigateToGameList -> sendEffect(HomeReducer.Effect.ShowGameList)

            is HomeReducer.Intent.NavigateToGameSeries -> sendEffect(HomeReducer.Effect.ShowGameSeries(intent.gamePk))

            is HomeReducer.Intent.NavigateToDetail -> sendEffect(HomeReducer.Effect.ShowDetail(intent.gamePk))
        }
    }

    private fun getGames() {
        viewModelScope.launch {
            sendEvent(HomeReducer.Event.GamesLoadingChanged(true))
            try {
                val games = repository.getGames()
                sendEvent(HomeReducer.Event.GamesLoaded(games))
            } catch (e: Exception) {
                sendEvent(HomeReducer.Event.GamesErrorReceived(e))
            } finally {
                sendEvent(HomeReducer.Event.GamesLoadingChanged(false))
            }
        }
    }
}