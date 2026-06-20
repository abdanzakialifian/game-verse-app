package com.gameverse.app.presentation.catalogue

import androidx.compose.runtime.Immutable
import com.gameverse.app.mvi.Reducer

class CatalogueReducer : Reducer<CatalogueReducer.State, CatalogueReducer.Event> {
    @Immutable
    sealed interface Intent : Reducer.ViewIntent {
        data class OnCategoryClicked(val id: String) : Intent
    }

    @Immutable
    sealed interface Event : Reducer.ViewEvent

    @Immutable
    sealed interface Effect : Reducer.ViewEffect {
        data class NavigateToGameList(val id: String) : Effect
    }

    @Immutable
    data class State(
        val data: String = ""
    ) : Reducer.ViewState

    override fun reduce(
        state: State,
        event: Event
    ): State = state
}