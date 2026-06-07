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
    override fun sendIntent(intent: HomeReducer.Intent) {
        when (intent) {
            is HomeReducer.Intent.OnSearchValueChanged -> sendEvent(
                HomeReducer.Event.SearchValueChanged(intent.value)
            )

            HomeReducer.Intent.OnGetGames -> getGames()
            is HomeReducer.Intent.OnExpanded -> sendEvent(
                HomeReducer.Event.Expanded(
                    id = intent.id,
                    isExpanded = intent.isExpanded
                )
            )

            HomeReducer.Intent.OnNavigateToGameList -> sendEffect(HomeReducer.Effect.NavigateToGameList)
        }
    }

    private fun getGames() {
        viewModelScope.launch {
            sendEvent(HomeReducer.Event.GetGamesLoading(true))
            try {
                val games = repository.getGames()
                sendEvent(HomeReducer.Event.GetGamesData(games))
            } catch (e: Exception) {
                sendEvent(HomeReducer.Event.GetGamesError(e))
            } finally {
                sendEvent(HomeReducer.Event.GetGamesLoading(false))
            }
        }
    }
}