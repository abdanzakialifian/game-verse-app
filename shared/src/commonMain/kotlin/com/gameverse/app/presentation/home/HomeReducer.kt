package com.gameverse.app.presentation.home

import androidx.compose.runtime.Immutable
import com.gameverse.app.domain.model.GamesModel
import com.gameverse.app.mvi.Reducer

class HomeReducer : Reducer<HomeReducer.State, HomeReducer.Event> {
    @Immutable
    sealed interface Intent : Reducer.ViewIntent {
        data class OnSearchValueChanged(val value: String) : Intent
        data object OnGetGames : Intent
        data class OnExpanded(val id: Int) : Intent
        data object OnNavigateToGameList : Intent
    }

    @Immutable
    sealed interface Event : Reducer.ViewEvent {
        data class SearchValueChanged(val value: String) : Event
        data class GetGamesLoading(val isLoading: Boolean) : Event
        data class GetGamesData(val data: List<GamesModel>) : Event
        data class GetGamesError(val error: Throwable) : Event
        data class Expanded(val id: Int) : Event
    }

    @Immutable
    sealed interface Effect : Reducer.ViewEffect {
        data object NavigateToGameList : Effect
    }

    @Immutable
    data class State(
        val searchValue: String = "",
        val isGamesLoading: Boolean = false,
        val gamesData: List<GamesModel> = emptyList(),
        val gamesError: Throwable? = null,
        val expandedIds: Set<Int> = emptySet(),
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
            is Event.SearchValueChanged -> state.copy(searchValue = event.value)
            is Event.GetGamesLoading -> state.copy(isGamesLoading = event.isLoading)
            is Event.GetGamesData -> state.copy(gamesData = event.data)
            is Event.GetGamesError -> state.copy(gamesError = event.error)
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