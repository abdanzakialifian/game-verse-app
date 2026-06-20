package com.gameverse.app.presentation.detail

import androidx.compose.runtime.Immutable
import com.gameverse.app.mvi.Reducer

class DetailReducer : Reducer<DetailReducer.State, DetailReducer.Event> {
    @Immutable
    sealed interface Intent : Reducer.ViewIntent

    @Immutable
    sealed interface Event : Reducer.ViewEvent

    @Immutable
    sealed interface Effect : Reducer.ViewEffect

    @Immutable
    data class State(
        val data: String = ""
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
            else -> state
        }
    }
}