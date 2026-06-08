package com.gameverse.app.presentation.games

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gameverse.app.domain.repository.GVRepository
import com.gameverse.app.mvi.BaseViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class GamesViewModel(
    repository: GVRepository
) : BaseViewModel<GamesListReducer.State, GamesListReducer.Event, GamesListReducer.Effect, GamesListReducer.Intent>(
    initialState = GamesListReducer.State(),
    reducer = GamesListReducer()
) {
    override fun sendIntent(intent: GamesListReducer.Intent) {
        when (intent) {
            is GamesListReducer.Intent.OnSearchValueChanged -> sendEvent(
                GamesListReducer.Event.SearchValueChanged(intent.value)
            )

            is GamesListReducer.Intent.OnExpanded -> sendEvent(
                GamesListReducer.Event.Expanded(intent.id)
            )
        }
    }

    val getGamesPaging = state
        .map { it.searchValue }
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { value ->
            repository.getGamesPaging(value)
        }
        .cachedIn(viewModelScope)
}