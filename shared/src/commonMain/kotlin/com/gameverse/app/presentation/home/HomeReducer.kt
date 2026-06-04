package com.gameverse.app.presentation.home

import androidx.compose.runtime.Immutable
import com.gameverse.app.mvi.Reducer

class HomeReducer : Reducer<HomeReducer.State, HomeReducer.Event> {
    @Immutable
    sealed interface Intent : Reducer.ViewIntent {
        data class OnSearchValueChanged(val value: String) : Intent
    }

    @Immutable
    sealed interface Event : Reducer.ViewEvent {
        data class SearchValueChanged(val value: String) : Event
    }

    @Immutable
    sealed interface Effect : Reducer.ViewEffect

    @Immutable
    data class State(
        val searchValue: String = "",
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State {
        return when (event) {
            is Event.SearchValueChanged -> state.copy(searchValue = event.value)
        }
    }
}