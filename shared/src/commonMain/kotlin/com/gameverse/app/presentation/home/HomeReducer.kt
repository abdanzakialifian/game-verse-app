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
        data object ShowGameList : Effect
        data class ShowGameSeries(val gamePk: String) : Effect
        data class ShowDetail(val gamePk: String) : Effect
    }

    @Immutable
    data class State(
        val query: String = "",
        val isLoading: Boolean = true,
        val games: List<GameModel> = emptyList(),
        val error: Throwable? = null,
        val expandedIds: Set<Int> = emptySet(),
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
            is Event.SearchValueChanged -> state.copy(query = event.value)
            is Event.GamesLoadingChanged -> state.copy(isLoading = event.isLoading)
            is Event.GamesLoaded -> state.copy(games = event.data, error = null)
            is Event.GamesErrorReceived -> state.copy(error = event.error)
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