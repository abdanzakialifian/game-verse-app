package com.gameverse.app.presentation.favorite

import com.gameverse.app.domain.model.GameModel
import com.gameverse.app.mvi.Reducer

class FavoriteReducer : Reducer<FavoriteReducer.State, FavoriteReducer.Event> {
    sealed interface Intent : Reducer.ViewIntent {
        data object OnGetFavorites : Intent
        data class OnExpanded(val id: Int) : Intent
        data class OnNavigateToGameSeries(val gamePk: String) : Intent
        data class OnNavigateToDetail(val gamePk: String) : Intent
    }

    sealed interface Event : Reducer.ViewEvent {
        data class GetFavoritesData(val data: List<GameModel>) : Event
        data class GetFavoritesError(val error: Throwable) : Event
        data class Expanded(val id: Int) : Event
    }

    sealed interface Effect : Reducer.ViewEffect {
        data class NavigateToGameSeries(val gamePk: String) : Effect
        data class NavigateToDetail(val gamePk: String) : Effect
    }

    data class State(
        val gameList: List<GameModel> = emptyList(),
        val error: Throwable? = null,
        val expandedIds: Set<Int> = emptySet(),
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
            is Event.GetFavoritesData -> state.copy(gameList = event.data)
            is Event.GetFavoritesError -> state.copy(error = event.error)
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