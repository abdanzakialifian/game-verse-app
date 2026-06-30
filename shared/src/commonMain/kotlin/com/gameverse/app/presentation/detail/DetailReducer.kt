package com.gameverse.app.presentation.detail

import androidx.compose.runtime.Immutable
import com.gameverse.app.domain.model.DetailModel
import com.gameverse.app.domain.model.MoviesModel
import com.gameverse.app.mvi.Reducer

class DetailReducer : Reducer<DetailReducer.State, DetailReducer.Event> {
    @Immutable
    sealed interface Intent : Reducer.ViewIntent {
        data class OnGetGamesMovies(val id: String) : Intent
        data class OnNavigateToDetailVideoPlayer(val url: String) : Intent
    }

    @Immutable
    sealed interface Event : Reducer.ViewEvent {
        data class GetGameDetailLoading(val isLoading: Boolean) : Event
        data class GetGameDetailData(val data: DetailModel) : Event
        data class GetGameDetailError(val error: Throwable) : Event
        data class GetMoviesLoading(val isLoading: Boolean) : Event
        data class GetMoviesData(val data: List<MoviesModel>) : Event
        data class GetMoviesError(val error: Throwable) : Event
    }

    @Immutable
    sealed interface Effect : Reducer.ViewEffect {
        data class NavigateToDetailVideoPlayer(val url: String) : Effect
    }

    @Immutable
    data class State(
        val isDetailLoading: Boolean = false,
        val detailData: DetailModel? = null,
        val detailError: Throwable? = null,
        val isMoviesLoading: Boolean = false,
        val moviesData: List<MoviesModel> = emptyList(),
        val moviesError: Throwable? = null
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
            is Event.GetGameDetailLoading -> state.copy(isDetailLoading = event.isLoading)
            is Event.GetGameDetailData -> state.copy(detailData = event.data)
            is Event.GetGameDetailError -> state.copy(detailError = event.error)
            is Event.GetMoviesLoading -> state.copy(isMoviesLoading = event.isLoading)
            is Event.GetMoviesData -> state.copy(moviesData = event.data)
            is Event.GetMoviesError -> state.copy(moviesError = event.error)
        }
    }
}