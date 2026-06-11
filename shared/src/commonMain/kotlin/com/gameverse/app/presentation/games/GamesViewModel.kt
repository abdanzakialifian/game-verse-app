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
) : BaseViewModel<GamesReducer.State, GamesReducer.Event, GamesReducer.Effect, GamesReducer.Intent>(
    initialState = GamesReducer.State(),
    reducer = GamesReducer()
) {
    override fun sendIntent(intent: GamesReducer.Intent) {
        when (intent) {
            is GamesReducer.Intent.OnSearchValueChanged -> sendEvent(
                GamesReducer.Event.SearchValueChanged(intent.value)
            )

            is GamesReducer.Intent.OnExpanded -> sendEvent(
                GamesReducer.Event.Expanded(intent.id)
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