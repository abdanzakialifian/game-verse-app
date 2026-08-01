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
        data class SetGameId(val id: String) : Event
        data class GetGameDetailLoading(val isLoading: Boolean) : Event
        data class GetGameDetailData(val data: DetailModel) : Event
        data class GetGameDetailError(val error: Throwable) : Event
        data class GetMoviesLoading(val isLoading: Boolean) : Event
        data class GetMoviesData(val data: List<String>) : Event
        data class GetMoviesError(val error: Throwable) : Event
        data class ExpandDescription(val isExpandDescription: Boolean) : Event
        data class TextOverflow(val isTextOverflowing: Boolean) : Event
        data class SetFavoriteStatus(val isFavorite: Boolean) : Event
    }

    @Immutable
    sealed interface Effect : Reducer.ViewEffect

    @Immutable
    data class State(
        val gameId: String = "",
        val isDetailLoading: Boolean = false,
        val detailData: DetailModel? = null,
        val detailError: Throwable? = null,
        val isMoviesLoading: Boolean = false,
        val moviesData: List<String> = emptyList(),
        val moviesError: Throwable? = null,
        val isTextOverflowing: Boolean = false,
        val isExpandDescription: Boolean = false,
        val isFavorite: Boolean = false,
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
            is Event.GetGameDetailLoading -> state.copy(isDetailLoading = event.isLoading)
            is Event.GetGameDetailData -> state.copy(detailData = event.data, detailError = null)
            is Event.GetGameDetailError -> state.copy(detailError = event.error)
            is Event.GetMoviesLoading -> state.copy(isMoviesLoading = event.isLoading)
            is Event.GetMoviesData -> state.copy(moviesData = event.data, moviesError = null)
            is Event.GetMoviesError -> state.copy(moviesError = event.error)
            is Event.ExpandDescription -> state.copy(isExpandDescription = event.isExpandDescription)
            is Event.TextOverflow -> {
                if (state.isTextOverflowing != event.isTextOverflowing) {
                    state.copy(isTextOverflowing = event.isTextOverflowing)
                } else {
                    state
                }
            }
            is Event.SetGameId -> state.copy(gameId = event.id)
            is Event.SetFavoriteStatus -> state.copy(isFavorite = event.isFavorite)
        }
    }
}