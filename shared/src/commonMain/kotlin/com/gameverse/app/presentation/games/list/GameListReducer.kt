package com.gameverse.app.presentation.games.list

import androidx.compose.runtime.Immutable
import com.gameverse.app.mvi.Reducer

class GameListReducer : Reducer<GameListReducer.State, GameListReducer.Event> {
    @Immutable
    sealed interface Intent : Reducer.ViewIntent {
        data class ToggleSearch(val isSearchVisible: Boolean) : Intent
        data class Search(val value: String) : Intent
        data class Expand(val id: Int) : Intent
        data class NavigateToGameSeries(val gamePk: String) : Intent
        data class NavigateToDetail(val id: Int) : Intent
        data object NavigateBack : Intent
    }

    @Immutable
    sealed interface Event : Reducer.ViewEvent {
        data class SearchVisibilityChanged(val isSearchVisible: Boolean) : Event
        data class SearchValueChanged(val value: String) : Event
        data class Expanded(val id: Int) : Event
    }

    @Immutable
    sealed interface Effect : Reducer.ViewEffect {
        data class NavigateToGameSeries(val gamePk: String) : Effect
        data class NavigateToDetailGame(val id: Int) : Effect
        data object NavigateBack : Effect
    }

    @Immutable
    data class State(
        val isSearchVisible: Boolean = false,
        val searchValue: String = "",
        val expandedIds: Set<Int> = emptySet(),
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
            is Event.SearchVisibilityChanged -> state.copy(isSearchVisible = event.isSearchVisible)
            is Event.SearchValueChanged -> state.copy(searchValue = event.value)
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