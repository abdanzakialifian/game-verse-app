package com.gameverse.app.presentation.games.series

import androidx.compose.runtime.Immutable
import com.gameverse.app.mvi.Reducer

class GamesSeriesReducer : Reducer<GamesSeriesReducer.State, GamesSeriesReducer.Event> {
    @Immutable
    sealed interface Intent : Reducer.ViewIntent {
        data class OnExpanded(val id: Int) : Intent
    }

    @Immutable
    sealed interface Event : Reducer.ViewEvent {
        data class Expanded(val id: Int) : Event
    }

    @Immutable
    sealed interface Effect : Reducer.ViewEffect

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