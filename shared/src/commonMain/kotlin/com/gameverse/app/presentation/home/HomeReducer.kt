package com.gameverse.app.presentation.home

import androidx.compose.runtime.Immutable
import com.gameverse.app.domain.model.GameModel
import com.gameverse.app.mvi.Reducer

class HomeReducer : Reducer<HomeReducer.State, HomeReducer.Event> {
    @Immutable
    sealed interface Intent : Reducer.ViewIntent {
        data class Search(val value: String) : Intent
        data object LoadGames : Intent
        data class Expand(val id: Int) : Intent
        data object NavigateToGameList : Intent
        data class NavigateToGameSeries(val gamePk: String) : Intent
        data class NavigateToDetail(val gamePk: String) : Intent
    }

    @Immutable
    sealed interface Event : Reducer.ViewEvent {
        data class SearchValueChanged(val value: String) : Event
        data class GamesLoadingChanged(val isLoading: Boolean) : Event
        data class GamesLoaded(val data: List<GameModel>) : Event
        data class GamesErrorReceived(val error: Throwable) : Event
        data class Expanded(val id: Int) : Event
    }

    @Immutable
    sealed interface Effect : Reducer.ViewEffect {
        data object NavigateToGameList : Effect
        data class NavigateToGameSeries(val gamePk: String) : Effect
        data class NavigateToDetail(val gamePk: String) : Effect
    }

    @Immutable
    data class State(
        val searchValue: String = "",
        val isGamesLoading: Boolean = true,
        val gamesData: List<GameModel> = emptyList(),
        val gamesError: Throwable? = null,
        val expandedIds: Set<Int> = emptySet(),
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
            is Event.SearchValueChanged -> state.copy(searchValue = event.value)
            is Event.GamesLoadingChanged -> state.copy(isGamesLoading = event.isLoading)
            is Event.GamesLoaded -> state.copy(gamesData = event.data, gamesError = null)
            is Event.GamesErrorReceived -> state.copy(gamesError = event.error)
            is Event.Expanded -> {
                val expandedIds = if (event.id in state.expandedIds) {
                    state.expandedIds - event.id
                } else {
                    state.expandedIds + event.id
                }
                state.copy(expandedIds = expandedIds)
            }
        }
    }
}