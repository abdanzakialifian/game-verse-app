package com.gameverse.app.presentation.detail

import androidx.compose.runtime.Immutable
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.mvi.Reducer

class DetailReducer : Reducer<DetailReducer.State, DetailReducer.Event> {
    @Immutable
    sealed interface Intent : Reducer.ViewIntent {
        data class LoadGameDetail(val id: String) : Intent
        data class LoadMovies(val id: String) : Intent
        data class ExpandDescription(val isExpandDescription: Boolean) : Intent
        data class TextOverflow(val isTextOverflowing: Boolean) : Intent
        data class Favorite(
            val isFavorite: Boolean,
            val detailModel: DetailModel
        ) : Intent
    }

    @Immutable
    sealed interface Event : Reducer.ViewEvent {
        data class GameIdChanged(val id: String) : Event
        data class GameDetailLoadingChanged(val isLoading: Boolean) : Event
        data class GameDetailLoaded(val data: DetailModel) : Event
        data class GameDetailErrorReceived(val error: Throwable) : Event
        data class MoviesLoadingChanged(val isLoading: Boolean) : Event
        data class MoviesLoaded(val data: List<String>) : Event
        data class MoviesErrorReceived(val error: Throwable) : Event
        data class ExpandDescriptionChanged(val isExpandDescription: Boolean) : Event
        data class TextOverflowChanged(val isTextOverflowing: Boolean) : Event
        data class FavoriteStatusChanged(val isFavorite: Boolean) : Event
    }

    @Immutable
    sealed interface Effect : Reducer.ViewEffect

    @Immutable
    data class State(
        val gameId: String = "",
        val isLoading: Boolean = false,
        val detail: DetailModel? = null,
        val error: Throwable? = null,
        val isMoviesLoading: Boolean = false,
        val movies: List<String> = emptyList(),
        val moviesError: Throwable? = null,
        val isTextOverflowing: Boolean = false,
        val isDescriptionExpanded: Boolean = false,
        val isFavorite: Boolean = false,
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
            is Event.GameDetailLoadingChanged -> state.copy(isLoading = event.isLoading)
            is Event.GameDetailLoaded -> state.copy(detail = event.data, error = null)
            is Event.GameDetailErrorReceived -> state.copy(error = event.error)
            is Event.MoviesLoadingChanged -> state.copy(isMoviesLoading = event.isLoading)
            is Event.MoviesLoaded -> state.copy(movies = event.data, moviesError = null)
            is Event.MoviesErrorReceived -> state.copy(moviesError = event.error)
            is Event.ExpandDescriptionChanged -> state.copy(isDescriptionExpanded = event.isExpandDescription)
            is Event.TextOverflowChanged -> {
                if (state.isTextOverflowing != event.isTextOverflowing) {
                    state.copy(isTextOverflowing = event.isTextOverflowing)
                } else {
                    state
                }
            }
            is Event.GameIdChanged -> state.copy(gameId = event.id)
            is Event.FavoriteStatusChanged -> state.copy(isFavorite = event.isFavorite)
        }
    }
}