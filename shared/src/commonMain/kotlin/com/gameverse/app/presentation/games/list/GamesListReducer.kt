package com.gameverse.app.presentation.games.list

import androidx.compose.runtime.Immutable
import com.gameverse.app.mvi.Reducer

class GamesListReducer : Reducer<GamesListReducer.State, GamesListReducer.Event> {
    @Immutable
    sealed interface Intent : Reducer.ViewIntent {
        data class OnSearchValueChanged(val value: String) : Intent
        data class OnExpanded(val id: Int) : Intent
        data class OnNavigateToGameSeries(val gamePk: String) : Intent
    }

    @Immutable
    sealed interface Event : Reducer.ViewEvent {
        data class SearchValueChanged(val value: String) : Event
        data class Expanded(val id: Int) : Event
    }

    @Immutable
    sealed interface Effect : Reducer.ViewEffect {
        data class NavigateToGameSeries(val gamePk: String) : Effect
    }

    @Immutable
    data class State(
        val searchValue: String = "",
        val expandedIds: Set<Int> = emptySet(),
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
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